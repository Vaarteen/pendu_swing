package gui;

import dao.DAOFactory;
import static gui.HangmanFrame.HOMEPANEL;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import models.Hangman;
import models.User;
import models.WordManager;

/**
 * Le panneau visuel du jeu de pendu.
 *
 * @author Herbert Caffarel
 */
public class GamePanel extends HangmanPanel {

    private static final long serialVersionUID = 1L;

    private transient final Hangman game; // Le jeu en cours
    private transient final WordManager wm; // Le gestionnaire de Word
    private final Box searchArea; // L'espace de recherche du mot
    private final JPanel wordPanel, // Lespace d'affichage du mot
            lettersPanel, // L'espace d'affichage des lettres proposées
            keyboardPanel; // L'espace d'affichage du clavier virtuel
    private final CenteredGameLabel imageLabel,// L'image de la pendaison
            wordIntro, // Le texte de présentation du mot à trouver
            wordLabel, // Le mot à trouver
            lettersIntro, // Le texte de présentation des lettres proposées
            lettersLabel; // La liste des lettres proposées
    private final GridLayout gridLayout; // Un gestionnaire de placement en grille à 2 lignes
    private final JButton[] keys; // Tous les boutons du clavier numérique

    /**
     * Constructeur.
     *
     * @param frame La JFrame parente de ce panneau.
     */
    public GamePanel(HangmanFrame frame) {
        super("Jeu", frame);
        // Initialisation des attributs
        game = new Hangman();
        wm = game.getWordManager();
        searchArea = Box.createVerticalBox();
        gridLayout = new GridLayout(2, 0);
        wordPanel = new JPanel(gridLayout);
        wordLabel = new CenteredGameLabel();
        wordIntro = new CenteredGameLabel("Trouve ce mot");
        lettersPanel = new JPanel(gridLayout);
        lettersLabel = new CenteredGameLabel();
        lettersIntro = new CenteredGameLabel("Les lettres proposées");
        imageLabel = new CenteredGameLabel();
        keyboardPanel = new JPanel(new GridLayout(3, 12, 5, 5));
        keys = new JButton[26];
        // Organisation du contenu visuel
        initGui();
    }

    /**
     * Organisation des ojets graphiques dans le panneau.
     */
    private void initGui() {
        showImage();
        showShadowedWord();
        showUsedLetters();
        createKeyboardPanel();
        // Ajout de l'espace de visualisation de la recherche
        wordPanel.add(wordIntro);
        wordPanel.add(wordLabel);
        lettersPanel.add(lettersIntro);
        lettersPanel.add(lettersLabel);
        searchArea.add(wordPanel);
        searchArea.add(lettersPanel);
        add(searchArea, BorderLayout.CENTER);
        add(imageLabel, BorderLayout.WEST);
        add(keyboardPanel, BorderLayout.SOUTH);
        // Gestion évènementielle
        initEvents();
    }

    /**
     * Mise en place des gestions évènementielles du panneau.
     */
    private void initEvents() {
        // Lors de l'affichage du panneau, si le jeu est terminé, on génère un
        // nouveau jeu.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (frame.getPlayer() == null) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Vous devez d'abord choisir un joueur !",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                    frame.getCardLayout().show(frame.getContent(), HOMEPANEL);
                } else if (game.isGameEnded()) {
                    newGame();
                }
            }
        });
    }

    /**
     * Affiche l'image correspondant à l'évolution de la pendaison. S'appuie sur
     * le nombre d'erreurs du jeu en cours.
     */
    private void showImage() {
        try {
            BufferedImage bi = ImageIO.read(GamePanel.class.getResourceAsStream(
                    "/images/pendu" + game.getErrorCnt() + ".jpg")
            );
            imageLabel.setIcon(new ImageIcon(bi));
        } catch (IOException ex) {
            Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Affiche le mot à trouver partiellement masqué dans le JLabel
     * correspondant.
     */
    private void showShadowedWord() {
        if (frame.getPlayer() != null) {
            wordLabel.setText(wm.getShadowedWord(game.getProposedLetters()));
        }
    }

    /**
     * Affiche la liste des lettres déjà proposées durant ce jeu dans l'espace
     * adequat.
     */
    private void showUsedLetters() {
        if (frame.getPlayer() != null) {
            StringBuilder sb = new StringBuilder();
            for (char c : game.getProposedLetters()) {
                sb.append(c);
            }
            lettersLabel.setText(sb.toString());
        }
    }

    /**
     * Crée le clavier virtuel avec sa gestion évènementielle. Une classe
     * interne pour la gestion des évènement a été créée, ainsi qu'un unique
     * exemplaire du gestionnaire d'évènement, puisque le traitement est
     * toujours le même, inutile de créer 26 objets !
     */
    private void createKeyboardPanel() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // Un écouteur pour tous les boutons
        KeySelectedListener kl = new KeySelectedListener();
        for (int i = 0; i < letters.length(); i++) {
            JButton key = new JButton("" + letters.charAt(i));
            keys[i] = key;
            key.addActionListener(kl);
            keyboardPanel.add(key);
        }
    }

    /**
     * Active ou désactive les boutons du clavier virtuel. Ceci afin d'éviter
     * que le joueur ne continue à enfoncer des boutons après la fin du jeu, ce
     * qui provoquerait des erreurs en cascade, et lui permettre de les
     * réutiliser lors d'un nouveau jeu.
     *
     * @param activate true pour activer les boutons, false pour les désactiver
     */
    private void setKeyboardActivation(boolean activate) {
        for (JButton key : keys) {
            key.setEnabled(activate);
        }
    }

    /**
     * Mise à jour de l'affichage du panneau. À utiliser après proposition d'une
     * lettre.
     */
    private void update() {
        // Afficher l'image correspondant au nombre d'erreur
        showImage();
        // Afficher le mot masqué
        showShadowedWord();
        // Afficher les lettres proposées
        showUsedLetters();
        // Selon l'état du jeu
        switch (game.checkState()) {
            case 2: // Jeu perdu
                looseGame();
                break;
            case 1: // Jeu gagné
                winGame();
        }
    }

    /**
     * Traitements lors de la perte du jeu.
     */
    private void looseGame() {
        // calcul du score rapporté par le mot
        int score = wm.getWord().length();
        // Aficher le message d'information
        String msg = "Vous avez maheureusement perdu\nLe mot à trouver était : " + wm.getWord();
        JOptionPane.showMessageDialog(frame, msg, "Perdu !", JOptionPane.ERROR_MESSAGE);
        // Désactiver le clavier virtuel
        setKeyboardActivation(false);
        // Ôter le score au joueur et le persister en DB
        User player = frame.getPlayer();
        player.setScore(player.getScore() - score);
        DAOFactory.getUserDao().persist(player);
        // Demander si on rejoue
        checkReplay();
    }

    /**
     * Traitements lors du gain du jeu.
     */
    private void winGame() {
        // calcul du score rapporté par le mot
        int score = wm.getWord().length();
        // Afficher le message d'information
        String msg = "Vous avez trouvé le mot caché, bravo !\nVous gagnez " + score + " points.";
        JOptionPane.showMessageDialog(frame, msg, "Gagné !", JOptionPane.INFORMATION_MESSAGE);
        // Désactiver le clavier virtuel
        setKeyboardActivation(false);
        // Ajouter le score au joueur et le persister en DB
        User player = frame.getPlayer();
        player.setScore(player.getScore() + score);
        DAOFactory.getUserDao().persist(player);
        // Demander si on rejoue
        checkReplay();
    }

    /**
     * Mise en place d'un nouveau jeu.
     */
    public void newGame() {
        game.newGame(frame.getPlayer()); // Nouveau mot à trouver.
        setKeyboardActivation(true); // Activer le clavier virtuel
        update(); // Mise à jour de l'affichage
    }

    /**
     * Demande si le joueur souhaite rejouer. Lance un nouveau jeu dans
     * l'affirmative, sinon affiche la page d'accueil.
     */
    private void checkReplay() {
        int replay = JOptionPane.showConfirmDialog(
                frame,
                "Voulez-vous rejouer ?",
                "Rejouer ?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (replay == JOptionPane.YES_OPTION) {
            newGame();
        } else {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.HOMEPANEL);
        }
    }

    /**
     * Classe de réponse à l'appui sur une touche du clavier virtuel.
     */
    private class KeySelectedListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // On récupère le bouton à l'origine de l'évènement
            JButton source = (JButton) e.getSource();
            // On récupère la lettre correspondante
            char letter = source.getText().charAt(0);
            // On propose la lettre au jeu
            game.proposeLetter(letter);
            // On désactive le bouton pour qu'il ne puisse pas être de nouveau sélectionné
            source.setEnabled(false);
            // On met l'affichage à jour
            update();
        }
    }

}

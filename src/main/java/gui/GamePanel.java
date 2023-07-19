package gui;

import dao.DAOFactory;
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

public class GamePanel extends HangmanPanel {

    private static final long serialVersionUID = 1L;

    private transient final Hangman game;
    private transient final WordManager wm;
    private final Box searchArea;
    private final JPanel wordPanel, lettersPanel, keyboardPanel;
    private final CenteredGameLabel imageLabel,// L'image de la pendaison
            wordIntro, // Le texte de présentation du mot à trouver
            wordLabel, // Le mot à trouver
            lettersIntro, // Le texte de présentation des lettres proposées
            lettersLabel; // La liste des lettres proposées
    private final GridLayout gridLayout;
    private final JButton[] keys;

    public GamePanel(HangmanFrame frame) {
        super("Jeu", frame);
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
        initGui();
    }

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
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (game.isGameEnded()) {
                    newGame();
                }
            }
        });
    }

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

    private void showShadowedWord() {
        wordLabel.setText(wm.getShadowedWord(game.getProposedLetters()));
    }

    private void showUsedLetters() {
        StringBuilder sb = new StringBuilder();
        for (char c : game.getProposedLetters()) {
            sb.append(c);
        }
        lettersLabel.setText(sb.toString());
    }

    private void createKeyboardPanel() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        KeySelectedListener kl = new KeySelectedListener();
        for (int i = 0; i < letters.length(); i++) {
            JButton key = new JButton("" + letters.charAt(i));
            keys[i] = key;
            key.addActionListener(kl);
            keyboardPanel.add(key);
        }
    }

    private void setKeyboardActivation(boolean activate) {
        for (JButton key : keys) {
            key.setEnabled(activate);
        }
    }

    private void update() {
        // Afficher l'image correspondant au nombre d'erreur
        showImage();
        // Afficher le mot masqué
        showShadowedWord();
        // Afficher les lettres proposées
        showUsedLetters();
        switch (game.checkState()) {
            case 2: // Jeu perdu
                looseGame();
                break;
            case 1: // Jeu gagné
                winGame();
        }
    }

    private void looseGame() {
        String msg = "Vous avez maheureusement perdu\nLe mot à trouver était : " + wm.getWord();
        JOptionPane.showMessageDialog(frame, msg, "Perdu !", JOptionPane.ERROR_MESSAGE);
        setKeyboardActivation(false);
        checkReplay();
    }

    private void winGame() {
        int score = wm.getWord().length();
        String msg = "Vous avez trouvé le mot caché, bravo !\nVous gagnez " + score + " points.";
        JOptionPane.showMessageDialog(frame, msg, "Gagné !", JOptionPane.INFORMATION_MESSAGE);
        setKeyboardActivation(false);
        User player = frame.getPlayer();
        player.setScore(player.getScore() + score);
        DAOFactory.getUserDao().persist(player);
        checkReplay();
    }

    public void newGame() {
        game.newGame();
        setKeyboardActivation(true);
        update();
    }

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

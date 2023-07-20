package gui;

import dao.DAOFactory;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import models.Word;

/**
 * Classe de menu spécifique au jeu. Ce menu est créé avec des touches d'accès
 * rapide (des touches de raccourci) ainsi que des Mnémniques, à savoir les
 * lettres associées à la sélection de l'item de menu.
 *
 * @author Herbert Caffarel
 */
public class HangmanMenubar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private final HangmanFrame frame; // La fenêtre associée à ce menu
    private final JMenu gameMenu, // Le menu "jeu"
            windowsMenu, // Le menu "fenêtres"
            adminMenu; // Le menu "administration"
    private final JMenuItem newGame, // nouveau jeu => crée un nouveau jeu
            newPlayer, // nouveau joueur => affiche la page d'accueil
            home, // page d'accueil
            hof, // page hall of fame
            game, // page du jeu
            addWord; // ajout d'un mot au dictionnaire => admin seulement

    /**
     * Constructeur.
     *
     * @param frame La fenêtre associée à ce menu
     */
    public HangmanMenubar(HangmanFrame frame) {
        this.frame = frame;
        windowsMenu = new JMenu("Fenêtre");
        gameMenu = new JMenu("Jeu");
        adminMenu = new JMenu("Administration");
        home = new JMenuItem("Accueil", KeyEvent.VK_A);
        hof = new JMenuItem("Hall of Fame", KeyEvent.VK_H);
        game = new JMenuItem("Jeu", KeyEvent.VK_J);
        newGame = new JMenuItem("Nouveau mot", KeyEvent.VK_N);
        newPlayer = new JMenuItem("Nouveau joueur", KeyEvent.VK_P);
        addWord = new JMenuItem("Ajouter un mot", KeyEvent.VK_W);
        // Organisation du contenu visuel
        initGui();
    }

    /**
     * Organisation des ojets graphiques dans le panneau.
     */
    private void initGui() {
        // Les mnémonique sur les menus
        windowsMenu.setMnemonic(KeyEvent.VK_F);
        gameMenu.setMnemonic(KeyEvent.VK_G);
        // Les raccourcis claviers des entrées de menu
        home.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.ALT_MASK));
        hof.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_H, ActionEvent.ALT_MASK));
        game.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_J, ActionEvent.ALT_MASK));
        newGame.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        newPlayer.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, ActionEvent.ALT_MASK));
        addWord.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, ActionEvent.ALT_MASK));
        // Ajout des entrées dans le menu windowsMenu
        windowsMenu.add(home);
        windowsMenu.add(hof);
        windowsMenu.add(game);
        // Ajout des entrées dans le menu gameMenu
        gameMenu.add(newGame);
        gameMenu.add(newPlayer);
        // Ajout des entrées dans le menu adminMenu
        adminMenu.add(addWord);
        // Ajout des menus à la barre de menus
        add(gameMenu);
        add(windowsMenu);
        add(adminMenu);
        // Gestion évènementielle
        initEvents();
    }

    /**
     * Mise en place des gestions évènementielles du panneau.
     */
    private void initEvents() {
        // Le menu accueil affiche l'accueil
        home.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.HOMEPANEL);
        });
        // Le menu hall of fame affiche le hall of fame
        hof.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.HOFPANEL);
        });
        // Le menu jeu affiche le jeu
        game.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.GAMEPANEL);
        });
        // création d'un nouveau jeu
        newGame.addActionListener((e) -> {
            frame.getGame().newGame();
        });
        // Changer de joueur => affiche l'accueil
        newPlayer.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.HOMEPANEL);
        });
        // Ajout d'un mot au dictionnaire => admin seulement
        addWord.addActionListener((e) -> {
            // Si pas admin on affiche un message d'erreur
            if (frame.getPlayer() == null || frame.getPlayer().getId() != 1) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Vous devez être administrateur !",
                        "Erreur !",
                        JOptionPane.ERROR_MESSAGE
                );
            } else { // Sinon on lance l'ajout d'un mot
                addWord();
            }
        });
    }

    /**
     * Ajout d'un mot au dictionnaire. Le mot est vérifié avant l'ajout pour
     * vérifier qu'il ne contient que des lettres acceptables et qu'il n'existe
     * pas déjà dans le dictionnaire.
     */
    private void addWord() {
        // Demander le mot à ajouter
        String word = JOptionPane.showInputDialog(
                frame,
                "Quel mot voulez-vous ajouter ?",
                "Ajout d'un mot",
                JOptionPane.QUESTION_MESSAGE
        );
        // Vérifier que le mot est valide
        if (word.matches("^[a-zA-Zàâäéèêëîïôöùûüÿç]+$")) {
            // Vérifier que le mot n'existe pas déjà dans le dictionnaire
            if (DAOFactory.getWordDao().getByWord(word) != null) { // si oui message informatif
                JOptionPane.showMessageDialog(
                        frame,
                        "Le mot " + word + " existe déjà dans le dictionnaire.",
                        "Inutile !",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else { // Si non ajout dans le dictionnaire et mesage d'info
                DAOFactory.getWordDao().persist(new Word(word));
                JOptionPane.showMessageDialog(
                        frame,
                        "Le mot " + word + " a été ajouté au dictionnaire.",
                        "Validé !",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else { // Mot non valide => message d'erreur
            JOptionPane.showMessageDialog(
                    frame,
                    "Ce mot est invalide car il ne comprend pas ques des lettres",
                    "Erreur !",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}

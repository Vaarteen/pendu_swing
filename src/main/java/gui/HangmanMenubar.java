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

public class HangmanMenubar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private final HangmanFrame frame;
    private final JMenu gameMenu,
            windowsMenu,
            adminMenu;
    private final JMenuItem newGame,
            newPlayer,
            home,
            hof,
            game,
            addWord;

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
        initGui();
    }

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
        // Initialisation de la gestion évènementielle
        initEvents();
    }

    private void initEvents() {
        home.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.HOMEPANEL);
        });
        hof.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.HOFPANEL);
        });
        game.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.GAMEPANEL);
        });
        newGame.addActionListener((e) -> {
            frame.getGame().newGame();
        });
        newPlayer.addActionListener((e) -> {
            frame.getCardLayout().show(frame.getContent(), HangmanFrame.HOMEPANEL);
        });
        addWord.addActionListener((e) -> {
            if (frame.getPlayer().getId() != 1) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Vous devez être administrateur !",
                        "Erreur !",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                addWord();
            }
        });
    }

    private void addWord() {
        String word = JOptionPane.showInputDialog(
                frame,
                "Quel mot voulez-vous ajouter ?",
                "Ajout d'un mot",
                JOptionPane.QUESTION_MESSAGE
        );
        if (word.matches("^[a-zA-Zàâäéèêëîïôöùûüÿç]+$")) {
            DAOFactory.getWordDao().persist(new Word(word));
            JOptionPane.showMessageDialog(
                    frame,
                    "Le mot " + word + " a été ajouté au dictionnaire.",
                    "Validé !",
                    JOptionPane.PLAIN_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Ce mot est invalide car il ne comprend pas ques des lettres",
                    "Erreur !",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class HangmanMenubar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private final HangmanFrame frame;
    private final JMenu gameMenu,
            windowsMenu;
    private final JMenuItem newGame,
            newPlayer,
            home,
            hof,
            game;

    public HangmanMenubar(HangmanFrame frame) {
        this.frame = frame;
        windowsMenu = new JMenu("Fenêtre");
        gameMenu = new JMenu("Jeu");
        home = new JMenuItem("Accueil", KeyEvent.VK_A);
        hof = new JMenuItem("Hall of Fame", KeyEvent.VK_H);
        game = new JMenuItem("Jeu", KeyEvent.VK_J);
        newGame = new JMenuItem("Nouveau mot", KeyEvent.VK_N);
        newPlayer = new JMenuItem("Nouveau joueur", KeyEvent.VK_P);
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
        // Ajout des entrées dans le menu windowsMenu
        windowsMenu.add(home);
        windowsMenu.add(hof);
        windowsMenu.add(game);
        // Ajout des entrées dans le menu gameMenu
        gameMenu.add(newGame);
        gameMenu.add(newPlayer);
        // Ajout des menus à la barre de menus
        add(gameMenu);
        add(windowsMenu);
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
    }

}

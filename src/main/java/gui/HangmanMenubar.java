package gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class HangmanMenubar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private final JPanel parent;
    private final JMenu windows;
    private final JMenuItem home;
    private final JMenuItem hof;
    private final JMenuItem game;

    public HangmanMenubar(JPanel parent) {
        this.parent = parent;
        windows = new JMenu("Fenêtre");
        home = new JMenuItem("Accueil", KeyEvent.VK_A);
        hof = new JMenuItem("Hall of Fame", KeyEvent.VK_H);
        game = new JMenuItem("Jeu", KeyEvent.VK_J);
        initGui();
    }

    private void initGui() {
        windows.setMnemonic(KeyEvent.VK_F);
        home.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.ALT_MASK));
        hof.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_H, ActionEvent.ALT_MASK));
        game.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_J, ActionEvent.ALT_MASK));
        home.addActionListener((e) -> {
            ((CardLayout) (parent.getLayout())).show(parent, "home");
        });
        hof.addActionListener((e) -> {
            ((CardLayout) (parent.getLayout())).show(parent, "hof");
        });
        game.addActionListener((e) -> {
            ((CardLayout) (parent.getLayout())).show(parent, "game");
        });
        windows.add(home);
        windows.add(hof);
        windows.add(game);
        add(windows);
    }

}

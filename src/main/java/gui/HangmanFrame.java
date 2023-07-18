package gui;

import java.awt.CardLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 *
 * @author Herbert Caffarel
 */
public class HangmanFrame extends JFrame {

    private static final String HOMEPANEL = "home";
    private static final String HOFPANEL = "hof";
    private static final String GAMEPANEL = "game";

    private final JPanel content;
    private final JPanel home, hof, game;
    private final JMenuBar menubar;

    public HangmanFrame() throws HeadlessException {
        super("Jouons au pendu");
        content = new JPanel();
        home = new HomePanel();
        hof = new HallOfFamePanel();
        game = new GamePanel();
        menubar = new HangmanMenubar(content);
        initGui(); // Initialisation du contenu
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fermeture du programme
        pack(); // Dimensionnement auto de la fenêtre
        setLocationRelativeTo(null); // Centrage de la fenêtre
        setVisible(true); // Affichage de la fenêtre
    }

    private void initGui() {
        content.setLayout(new CardLayout());
        content.add(home, HOMEPANEL);
        content.add(hof, HOFPANEL);
        content.add(game, GAMEPANEL);
        setContentPane(content);
        setJMenuBar(menubar);
    }

    public JPanel getHome() {
        return home;
    }

    public JPanel getHof() {
        return hof;
    }

    public JPanel getGame() {
        return game;
    }

}

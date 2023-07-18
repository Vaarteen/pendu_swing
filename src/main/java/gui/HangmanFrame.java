package gui;

import configuration.GameConstants;
import java.awt.CardLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private final HomePanel home;
    private final HallOfFamePanel hof;
    private final GamePanel game;
    private final JMenuBar menubar;

    static { // Ce code s'exécute AVANT le constructeur
        // Récupération de l'environnement graphique de programme
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Ajout de la fonte personnalisée (créée dans les constantes du jeu)
        // à l'interface graphique, donc valide pour TOUT le jeu
        ge.registerFont(GameConstants.GAME_FONT);
        // La fonte est maintenant utilisable avec un new Font("retro flower"...)
        // dans tout le projet
    }

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
        // Mise à jour du hall of fame lors de son affichage
        hof.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                hof.showList();
            }
        });
        setContentPane(content);
        setJMenuBar(menubar);
    }

    public HomePanel getHome() {
        return home;
    }

    public HallOfFamePanel getHof() {
        return hof;
    }

    public GamePanel getGame() {
        return game;
    }

}
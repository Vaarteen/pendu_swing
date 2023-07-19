package gui;

import configuration.GameConstants;
import configuration.Helpers;
import java.awt.CardLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import models.User;

/**
 *
 * @author Herbert Caffarel
 */
public class HangmanFrame extends JFrame {

    public static final String HOMEPANEL = "home";
    public static final String HOFPANEL = "hof";
    public static final String GAMEPANEL = "game";

    private final JPanel content;
    private final HomePanel home;
    private final HallOfFamePanel hof;
    private final GamePanel game;
    private final JMenuBar menubar;
    private final CardLayout cardLayout;
    private User player;

    static { // Ce code s'exécute AVANT le constructeur
        // Récupération de l'environnement graphique de programme
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Ajout de la fonte personnalisée (créée dans les constantes du jeu)
        // à l'interface graphique, donc valide pour TOUT le jeu
        ge.registerFont(GameConstants.GAME_FONT);
        // La fonte est maintenant utilisable avec un new Font("retro flower"...)
        // dans tout le projet
        Helpers.activateDB();
    }

    public HangmanFrame() throws HeadlessException {
        super("Jouons au pendu");
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        home = new HomePanel(this);
        hof = new HallOfFamePanel(this);
        game = new GamePanel(this);
        menubar = new HangmanMenubar(content);
        initGui(); // Initialisation du contenu
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fermeture du programme
        pack(); // Dimensionnement auto de la fenêtre
        setLocationRelativeTo(null); // Centrage de la fenêtre
        // Affichage de la fenêtre dans un thread séparé car Swing n'est pas
        // thread safe, il est mieux de le lancer dans son propre thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }

    private void initGui() {
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
        game.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (player == null) {
                    JOptionPane.showMessageDialog(
                            HangmanFrame.this,
                            "Vous devez d'abord choisir un joueur !",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                    cardLayout.show(content, HOMEPANEL);
                }
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

    public JPanel getContent() {
        return content;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

}

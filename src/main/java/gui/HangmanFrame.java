package gui;

import configuration.GameConstants;
import configuration.Helpers;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JButton;
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

    private final JPanel content, buttons;
    private final HomePanel home;
    private final HallOfFamePanel hof;
    private final GamePanel game;
    private final JMenuBar menubar;
    private final CardLayout cardLayout;
    private final JButton homeBtn, hofBtn, gameBtn;
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
        hof = new HallOfFamePanel(this);
        home = new HomePanel(this);
        game = new GamePanel(this);
        menubar = new HangmanMenubar(content);
        homeBtn = new JButton("Accueil");
        hofBtn = new JButton("Hall of Fame");
        gameBtn = new JButton("Jouer");
        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        initGui(); // Initialisation du contenu
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fermeture du programme
        pack(); // Dimensionnement auto de la fenêtre
        setLocationRelativeTo(null); // Centrage de la fenêtre
        // Affichage de la fenêtre dans un thread séparé car Swing n'est pas
        // thread safe, il est mieux de le lancer dans son propre thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
                cardLayout.show(content, HOMEPANEL);
            }
        });
    }

    private void initGui() {
        setLayout(new BorderLayout());
        // Ajout des cards dans le content
        content.add(hof, HOFPANEL);
        content.add(home, HOMEPANEL);
        content.add(game, GAMEPANEL);
        // Ajout des boutons dans le panel des boutons
        buttons.add(homeBtn);
        buttons.add(hofBtn);
        buttons.add(gameBtn);
        // Ajout des panel dans le content pane
        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        // Ajout du menu
        setJMenuBar(menubar);
        // Ajout de la gestion évènementielle
        initEvents();
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

    private void initEvents() {
        // Mise à jour du hall of fame lors de son affichage
        hof.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                hof.showList();
            }
        });
        // Un joueur doit être sélectionné pour pouvoir jouer
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
        // Affichage de la page d'accueil
        homeBtn.addActionListener((e) -> {
            cardLayout.show(content, HOMEPANEL);
        });
        // Affichage de la page de jeu
        gameBtn.addActionListener((e) -> {
            cardLayout.show(content, GAMEPANEL);
        });
        // Affichage du hall of fame
        hofBtn.addActionListener((e) -> {
            cardLayout.show(content, HOFPANEL);
        });
    }

}

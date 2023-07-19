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
 * La fenêtre principale du jeu. Sur la base d'un contenu principal en
 * CardLayout.
 *
 * @author Herbert Caffarel
 */
public class HangmanFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Des constantes pour simplifier l'écriture. À noter que je ne les ai pas
    // systématiquement utilisées, ce qui est mal.
    public static final String HOMEPANEL = "home";
    public static final String HOFPANEL = "hof";
    public static final String GAMEPANEL = "game";

    private final JPanel content, // L'espace d'affichage principal
            buttons; // L'espaces d'affichage des boutons pour accéder aux différentes vues.
    private final HomePanel home; // Le panneau d'afficahge de l'accueil
    private final HallOfFamePanel hof; // Le panneau d'affichage du hall of fame
    private final GamePanel game; // Le panneau d'afficahge du jeu
    private final JMenuBar menubar; // La barre de menu
    private final CardLayout cardLayout; // Le gestionnaire de placement du contenu principal
    private final JButton homeBtn, // Le bouton d'accès à l'accueil
            hofBtn, // Le bouton d'accès au hall of fame
            gameBtn; // Le bouton d'accès au jeu
    private transient User player; // Le joueur en cours

    static { // Ce code s'exécute AVANT le constructeur
        // Ajout de la fonte en ressource utilisable dans le jeu
        // Récupération de l'environnement graphique de programme
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Ajout de la fonte personnalisée (créée dans les constantes du jeu)
        // à l'interface graphique, donc valide pour TOUT le jeu
        ge.registerFont(GameConstants.GAME_FONT);
        // La fonte est maintenant utilisable avec un new Font("retro flower"...)
        // dans tout le projet
        // Activation de la DB : si elle n'existe pas, elle est créée avec des
        // valeurs par défaut
        Helpers.activateDB();
    }

    /**
     * Constructeur.
     *
     * @throws HeadlessException
     */
    public HangmanFrame() throws HeadlessException {
        super("Jouons au pendu");
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        hof = new HallOfFamePanel(this);
        home = new HomePanel(this);
        game = new GamePanel(this);
        menubar = new HangmanMenubar(this);
        homeBtn = new JButton("Accueil");
        hofBtn = new JButton("Hall of Fame");
        gameBtn = new JButton("Jouer");
        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Organisation du contenu visuel
        initGui();
        // À ne pas oublier : comment se comporte la fenêtre lors de sa fermeture !
        // par défaut, elle ne fait rien, donc le programme continue sans interface...
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Dimensionnement auto de la fenêtre
        pack();
        // Centrage de la fenêtre
        setLocationRelativeTo(null);
        // Affichage de la fenêtre dans un thread séparé car Swing n'est pas
        // thread safe, il est mieux de le lancer dans son propre thread
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            // Affichage de la page d'accueil. C'est nécessaire car lors de son
            // affichage la liste des joueurs est mise à jour automatiquement.
            cardLayout.show(content, HOMEPANEL);
        });
    }

    /**
     * Organisation des ojets graphiques dans le panneau.
     */
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
        // Gestion évènementielle
        initEvents();
    }

    /**
     * Mise en place des gestions évènementielles du panneau.
     */
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
        // Affichage de la page d'accueil sur clic du bouton home
        homeBtn.addActionListener((e) -> {
            cardLayout.show(content, HOMEPANEL);
        });
        // Affichage de la page de jeu sur clic du bouton game
        gameBtn.addActionListener((e) -> {
            cardLayout.show(content, GAMEPANEL);
        });
        // Affichage du hall of fame sur clic du bouton hall of fame
        hofBtn.addActionListener((e) -> {
            cardLayout.show(content, HOFPANEL);
        });
    }

    /* Getters et setters */
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

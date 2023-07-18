package gui;

import dao.DAOFactory;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import models.User;

public class HomePanel extends HangmanPanel {

    private final JPanel content, // Le panneau principal
            playerSelection, // Le panel du choix du joueur
            buttons; // Le panneau des boutons
    private final JButton play, hof;
    private final JComboBox<User> usersDropdown;
    private static final User NEW_USER = new User("Nouveau joueur");
    private User user;

    public HomePanel() {
        super("Accueil");
        content = new JPanel(new BorderLayout());
        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerSelection = new JPanel(); // FlowLayout par défaut
        usersDropdown = new JComboBox<>();
        play = new JButton("Jouer");
        hof = new JButton("Hall of Fame");
        initGui();
    }

    private void initGui() {
        // Ajout de l'image de présentation 
        try {
            BufferedImage bi = ImageIO.read(HomePanel.class.getResourceAsStream("/images/accueil.jpg"));
            ImageIcon image = new ImageIcon(bi);
            JLabel imageLabel = new JLabel(image);
            content.add(imageLabel, BorderLayout.WEST);
        } catch (IOException ex) {
            Logger.getLogger(HomePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Les joueurs
        fillUsers();
        playerSelection.add(usersDropdown);
        usersDropdown.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getItem() != NEW_USER) { // Si on a choisi un utilisateur
                    play.setEnabled(true); // On autorise le bouton pour jouer
                    user = (User) e.getItem(); // On récupère l'utilisateur
                    return; // On met fin à la sélection
                }
                // Si on a choisit un nouveau joueur
                // on demande son nom
                String userName = JOptionPane.showInputDialog("Quel est le nom du joueur ?");
                // On vérifie s'il existe déjà dans la DB auquel cas on
                // demande confirmation pour le prendre
                user = DAOFactory.getUserDao().getByName(userName);
                if (user == null || JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null,
                        "Cet utilisateur existe déjà !\nSouhaitez-vous jouer en tant que\n" + user + " ?",
                        "Confirmez votre choix", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    return; // Pas de confirmation, on sort sans rien faire
                }
                // Enfin on crée et on persiste en DB un nouveau joueur dans les autres cas
                user = new User(userName);
                DAOFactory.getUserDao().persist(user);
            }
        });
        // Les boutons
        play.setEnabled(false); // Inaccessible tant qu'on n'a pas choisi de joueur
        play.addActionListener((e) -> {
            HangmanFrame frame = (HangmanFrame) SwingUtilities.getRoot(play);
            CardLayout cl = frame.getCardLayout();
            cl.show(frame.getContent(), "game");
        });
        hof.addActionListener((e) -> {
            HangmanFrame frame = (HangmanFrame) SwingUtilities.getRoot(play);
            CardLayout cl = frame.getCardLayout();
            cl.show(frame.getContent(), "hof");
        });
        buttons.add(play);
        buttons.add(hof);

        // Ajout des panels au content
        add(playerSelection, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void fillUsers() {
        usersDropdown.removeAllItems();
        usersDropdown.addItem(NEW_USER);
        for (User user : DAOFactory.getUserDao().getAllByName()) {
            usersDropdown.addItem(user);
        }
    }
}

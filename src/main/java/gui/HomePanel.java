package gui;

import dao.DAOFactory;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import models.User;

public class HomePanel extends HangmanPanel {

    private final JPanel content, // Le panneau principal
            buttons, // Le panneau des boutons
            usersDropdownPanel; // Le paneau qui contient le dropdown de sélection du joueur
    private final Box playerSelection; // Le panel du choix du joueur
    private final JLabel playerSelectionLabel;
    private final JButton play, hof;
    private final JComboBox usersDropdown;
    private User player;

    public HomePanel(HangmanFrame frame) {
        super("Accueil", frame);
        player = frame.getPlayer();
        content = new JPanel(new BorderLayout());
        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerSelection = Box.createVerticalBox();
        playerSelectionLabel = new JLabel("Qui joue ?");
        usersDropdown = new JComboBox();
        usersDropdownPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
        usersDropdown.setEditable(true);
        usersDropdown.addItemListener(new UsersDropdownListener());
        playerSelectionLabel.setFont(new Font("Retro Flower", Font.ITALIC | Font.BOLD, 48));
        playerSelectionLabel.setForeground(Color.GREEN);
        playerSelectionLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        playerSelection.add(playerSelectionLabel);
        usersDropdownPanel.add(usersDropdown);
        playerSelection.add(usersDropdownPanel);
        // Les boutons
        play.setEnabled(false); // Inaccessible tant qu'on n'a pas choisi de joueur
        play.addActionListener(
                (e) -> {
                    if (player != null) {
                        CardLayout cl = frame.getCardLayout();
                        cl.show(frame.getContent(), "game");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Vous devez choisir un joueur d'abord !");
                    }
                }
        );
        hof.addActionListener(
                (e) -> {
                    CardLayout cl = frame.getCardLayout();
                    cl.show(frame.getContent(), "hof");
                }
        );
        buttons.add(play);
        buttons.add(hof);

        // Ajout des panels au content
        add(playerSelection, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void fillUsers() {
        usersDropdown.removeAllItems();
        usersDropdown.addItem("");
        for (User user : DAOFactory.getUserDao().getAllByName()) {
            usersDropdown.addItem(user);
        }
    }

    private class UsersDropdownListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            // Sur désélection on ne fait rien
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            // Sur sélection on teste
            if (usersDropdown.getSelectedItem() instanceof User) { // Joueur pris dans la liste
                player = (User) usersDropdown.getSelectedItem(); // On récupère l'utilisateur
            } else {
                // Sinon c'est une valeur (String) entrée par l'utilisateur
                // On vérifie si ce nom existe déjà dans la DB auquel cas on
                // demande confirmation pour le prendre... et on recommence jusqu'à
                // être dans un état acceptable
                String userName = (String) usersDropdown.getSelectedItem();
                boolean playerOk = false;
                do {
                    player = DAOFactory.getUserDao().getByName(userName);
                    if (player == null) { // L'utilisateur n'existe pas => on le crée et le persiste en DB
                        player = new User(userName);
                        DAOFactory.getUserDao().persist(player);
                        playerOk = true;
                    } else if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
                            frame,
                            "Cet utilisateur existe déjà !\nSouhaitez-vous jouer en tant que\n" + player + " ?",
                            "Confirmez votre choix",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE)) { // Le joueur existe en DB, on le propose à l'utilisateur
                        // S'il accepte, on garde le joueur et on sort de la boucle
                        playerOk = true;
                    } else { // Sinon on demande un nouveau nom
                        userName = JOptionPane.showInputDialog(frame, "Entrez un nouveau nom");
                    }
                } while (!playerOk); // Jusqu'à avoir un joueur valide
            }
            activatePlayer(player);
        }

        private void activatePlayer(User player) {
            frame.setPlayer(player);
            play.setEnabled(true);
        }
    }
}

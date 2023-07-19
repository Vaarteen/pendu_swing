package gui;

import dao.DAOFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import models.User;

public class HomePanel extends HangmanPanel {

    private final JPanel content, // Le panneau principal
            usersDropdownPanel; // Le paneau qui contient le dropdown de sélection du joueur
    private final Box playerSelection; // Le panel du choix du joueur
    private final CenteredGameLabel playerSelectionLabel;
    private final JComboBox playersDropdown;
    private final UsersDropdownListener udl;

    public HomePanel(HangmanFrame frame) {
        super("Accueil", frame);
        content = new JPanel(new BorderLayout());
        playerSelection = Box.createVerticalBox();
        playerSelectionLabel = new CenteredGameLabel("Qui joue ?");
        playersDropdown = new JComboBox();
        usersDropdownPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        udl = new UsersDropdownListener();
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
        playersDropdown.setEditable(true);
        playerSelectionLabel.getLabel().setForeground(Color.GREEN);
        playerSelectionLabel.getLabel().setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        playerSelection.add(playerSelectionLabel);
        usersDropdownPanel.add(playersDropdown);
        playerSelection.add(usersDropdownPanel);
        // Ajout des panels au content
        add(playerSelection, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);
        initEvents();
    }

    private void initEvents() {
        //  l'affichage de la card on met à jour la liste des joueurs
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                fillUsers();
            }

        });
    }

    private void fillUsers() {
        playersDropdown.removeItemListener(udl);
        playersDropdown.removeAllItems();
        playersDropdown.addItem("");
        for (User player : DAOFactory.getUserDao().getAllByName()) {
            playersDropdown.addItem(player);
            if (frame.getPlayer() != null && frame.getPlayer().getId() == player.getId()) {
                frame.setPlayer(player);
            }
        }
        playersDropdown.setSelectedItem(frame.getPlayer());
        playersDropdown.addItemListener(udl);
    }

    private void activatePlayer(User player) {
        frame.setPlayer(player);
        fillUsers();
        frame.setPlayer(player);
    }

    private class UsersDropdownListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            User player;
            // Sur désélection on ne fait rien
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            // Sur sélection on teste
            if (playersDropdown.getSelectedItem() instanceof User) { // Joueur pris dans la liste
                player = (User) playersDropdown.getSelectedItem(); // On récupère l'utilisateur
            } else {
                // Sinon c'est une valeur (String) entrée par l'utilisateur
                // On vérifie si ce nom existe déjà dans la DB auquel cas on
                // demande confirmation pour le prendre... et on recommence jusqu'à
                // être dans un état acceptable
                String userName = (String) playersDropdown.getSelectedItem();
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
    }
}

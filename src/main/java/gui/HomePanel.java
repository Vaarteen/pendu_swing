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

/**
 * Le panneau visuel de la page d'accueil. Dans cette classe je crée une classe
 * interne qui est un écouteur de liste déroulante. Pourquoi ? Parce que ja vais
 * ajouter et supprimer cet écouteur sur la liste selon les cas, et par
 * conséquent je dois avoir un écouteur nommé, donc une classe concrète.
 *
 * @author Herbert Caffarel
 */
public class HomePanel extends HangmanPanel {

    private static final long serialVersionUID = 1L;

    private final JPanel content, // Le panneau principal
            usersDropdownPanel; // Le paneau qui contient le dropdown de sélection du joueur
    private final Box playerSelection; // Le panel du choix du joueur
    private final CenteredGameLabel playerSelectionLabel; // Le label du sélecteur de joueur
    private final JComboBox playersDropdown; // Le sélecteur de joueur
    private final transient UsersDropdownListener udl; // L'écouteur.

    public HomePanel(HangmanFrame frame) {
        super("Accueil", frame);
        content = new JPanel(new BorderLayout());
        playerSelection = Box.createVerticalBox();
        playerSelectionLabel = new CenteredGameLabel("Qui joue ?");
        playersDropdown = new JComboBox();
        usersDropdownPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        udl = new UsersDropdownListener();
        // Organisation du contenu visuel
        initGui();
    }

    /**
     * Organisation des ojets graphiques dans le panneau.
     */
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
        // La liste des joueurs. Elle doit être générée systématiquement, donc
        // ici je ne mets que le composant grapique, il sera rempli ailleurs.
        // Ce dropdown est éditable, donc on peut taper du texte dedans, pas
        // seulement choisir une valeur existante dans la liste
        playersDropdown.setEditable(true);
        // Couleur de la fonte et bordure pour éviter les caractères coupés
        playerSelectionLabel.getLabel().setForeground(Color.GREEN);
        playerSelectionLabel.getLabel().setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        playerSelection.add(playerSelectionLabel);
        usersDropdownPanel.add(playersDropdown);
        playerSelection.add(usersDropdownPanel);
        // Ajout des panels au content
        add(playerSelection, BorderLayout.EAST);
        add(content, BorderLayout.CENTER);
        // Gestion évènementielle
        initEvents();
    }

    /**
     * Mise en place des gestions évènementielles du panneau.
     */
    private void initEvents() {
        //  l'affichage de la card on met à jour la liste des joueurs
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                fillUsers();
            }

        });
    }

    /**
     * Remplit la liste des joueurs.
     */
    @SuppressWarnings({"BoxedValueEquality", "NumberEquality"})
    private void fillUsers() {
        // On supprime l'écouteur car chaque ajout dans la liste provoquera un
        // évènement dont je ne veux pas, d'une part parce que c'est lourd et
        // inutile, d'autre part parce que ça provoque une boucle infinie d'évènements !
        playersDropdown.removeItemListener(udl);
        // Vider la liste
        playersDropdown.removeAllItems();
        // Ajouter un élément vide pour pouvoir taper un nom au choix
        playersDropdown.addItem("");
        // Ajouter tous les utilisateurs par ordre alphabétique
        for (User player : DAOFactory.getUserDao().getAllByName()) {
            playersDropdown.addItem(player);
            // Au passage si on a le même joueur que le joueur courant, on le
            // resélectionne (car son pointeur a changé, c'est une nouvelle liste !)
            // ça permet d'avoir l'affichage qui correspond au joueur et de ne
            // pas créer des joueurs qui n'existent pas...
            if (frame.getPlayer() != null && frame.getPlayer().getId() == player.getId()) {
                frame.setPlayer(player);
            }
        }
        // On présélectionne le joueur courant pour avoir un affichage qui suit le joueur
        playersDropdown.setSelectedItem(frame.getPlayer());
        // On remet l'écouteur de sélection
        playersDropdown.addItemListener(udl);
    }

    /**
     * Associe le joueur fourni au jeu courant.
     *
     * @param player Le joueur à associer au jeu
     */
    private void activatePlayer(User player) {
        frame.setPlayer(player);
        // On réaffiche la liste des joueurs avec le joueur présélectionnée
        fillUsers();
    }

    /**
     * Classe interne d'un écouteur de liste déroulante. Lors de la sélection
     * d'un joueur, on prend ce joueur. Si en revanche c'est une nouvelle valeur
     * tapée au clavier, on vérifie d'abord si ce nom existe déjà auquel cas on
     * le propose à l'utilisateur. S'il refuse, on demande un nouveau nom. Pour
     * un nouveau nom on le persiste immédiatement dans la DB.
     */
    private class UsersDropdownListener implements ItemListener {

        /**
         * Lors d'un sélection, il iy a 2 évènements : la désélection de
         * l'ancienne valeur, et la sélection de la nouvelle valeur. On ne
         * travaille que sur la sélection.
         *
         * @param e
         */
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

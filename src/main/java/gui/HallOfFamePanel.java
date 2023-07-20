package gui;

import dao.DAOFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.User;

/**
 * Le panneau visuel du Hall of Fame.
 *
 * @author Herbert Caffarel
 */
public class HallOfFamePanel extends HangmanPanel {

    private static final long serialVersionUID = 1L;

    private final Box listPanel; // L'espace de visualisation de la liste
    private final Font firstRankFont, // La fonte pour le premier joueur
            secondRankFont, // La fonte pour le second
            thirdRankFont, // La fonte pour le troisième
            otherRankFont; // La fonte pour les autres joueurs
    private transient List<User> users; // La liste des 10 joueurs

    public HallOfFamePanel(HangmanFrame frame) {
        super("Hall of Fame", frame);
        // Récupérer les 10 premiers
        users = DAOFactory.getUserDao().getHallOfFame();
        listPanel = Box.createVerticalBox();
        firstRankFont = new Font("Retro Flower", Font.ITALIC | Font.BOLD, 48);
        secondRankFont = new Font("Retro Flower", Font.ITALIC | Font.BOLD, 36);
        thirdRankFont = new Font("Retro Flower", Font.ITALIC | Font.BOLD, 30);
        otherRankFont = new Font("Retro Flower", Font.PLAIN, 24);
        // Organisation du contenu visuel
        initGui();
    }

    /**
     * Organisation des ojets graphiques dans le panneau.
     */
    private void initGui() {
        // Ajouter l'espace de visualisation de la liste dans le panneau
        add(listPanel, BorderLayout.CENTER);
        // Afficher la liste
        showList();
        // Gestion évènementielle
        initEvents();
    }

    /**
     * Mise en place des gestions évènementielles du panneau.
     */
    private void initEvents() {
        // Lors de l'affichage on réinitialise le contenu de la liste
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                showList();
            }
        });
    }

    /**
     * Affiche la liste des 10 premiers joueurs par score.
     */
    public void showList() {
        // On vide le visuel
        listPanel.removeAll();
        // On récupère le hall of fame
        users = DAOFactory.getUserDao().getHallOfFame();
        // On le rajoute dans le visuel
        for (int i = 0; i < users.size(); i++) {
            JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel userLabel = new JLabel(users.get(i).toString());
            // Selon le rang on utilise une fonte différente
            switch (i) {
                case 0:
                    userLabel.setFont(firstRankFont);
                    break;
                case 1:
                    userLabel.setFont(secondRankFont);
                    break;
                case 2:
                    userLabel.setFont(thirdRankFont);
                    break;
                default:
                    userLabel.setFont(otherRankFont);
            }
            userPanel.add(userLabel);
            userLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            // On ajoute la panl à la liste
            listPanel.add(userPanel);
        }
        // On réaffiche le composant parce qu'il a été modifié
        validate();
        repaint();
    }
}

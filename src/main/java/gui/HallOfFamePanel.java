package gui;

import dao.DAOFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.User;

public class HallOfFamePanel extends HangmanPanel {

    private static final long serialVersionUID = 1L;

    private final Box listPanel;
    private final Font firstRankFont, secondRankFont, thirdRankFont, otherRankFont;
    private transient List<User> users;

    public HallOfFamePanel(HangmanFrame frame) {
        super("Hall of Fame", frame);
        users = DAOFactory.getUserDao().getHallOfFame();
        listPanel = Box.createVerticalBox();
        firstRankFont = new Font("Retro Flower", Font.ITALIC | Font.BOLD, 48);
        secondRankFont = new Font("Retro Flower", Font.ITALIC | Font.BOLD, 36);
        thirdRankFont = new Font("Retro Flower", Font.ITALIC | Font.BOLD, 24);
        otherRankFont = new Font("Retro Flower", Font.PLAIN, 24);
        initGui();
    }

    private void initGui() {
        add(listPanel, BorderLayout.CENTER);
        showList();
    }

    public void showList() {
        // On vide le visuel
        listPanel.removeAll();
        // On récupère le hall of fame
        users = DAOFactory.getUserDao().getHallOfFame();
        // On le rajoute dans le visuel
        for (int i = 0; i < users.size(); i++) {
            JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel userLabel = new JLabel(users.get(i).toString());
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
        // On réaffiche le composant
        validate();
        repaint();
    }
}

package gui;

import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Un JLabel centré dans un JPanel avec la fonte personnalisée du jeu.
 *
 * @author Herbert Caffarel
 */
public class CenteredGameLabel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Font FONT = new Font("retro flower", Font.BOLD, 36);

    private final JLabel label;

    public CenteredGameLabel() {
        super(new FlowLayout(FlowLayout.CENTER));
        label = new JLabel();
        label.setFont(FONT);
        add(label);
    }

    public CenteredGameLabel(String text) {
        this();
        label.setText(text);
    }

    /**
     * Ajoute du texte au JLabel. Méthode de convénience, on peut passer par le
     * getter du JLabel.
     *
     * @param text Le texte à ajouter au label
     */
    public void setText(String text) {
        label.setText(text);
    }

    /**
     * Ajoute une icône au JLabel. Méthode de convénience, on peut passer par le
     * getter du JLabel.
     *
     * @param icon L'icône à ajouter au JLabel
     */
    void setIcon(Icon icon) {
        label.setIcon(icon);
    }

    /**
     * Getter du JLabel contenu dans l'objet.
     *
     * @return Le JLabel de l'objet
     */
    public JLabel getLabel() {
        return label;
    }

}

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Classe mère décrivant une carte à ajouter à l'affichage principal du jeu.
 * L'intérêt est de systématiser l'affichage de l'entête de la page (le titre)
 * et de fournir la fonte personnalisée.
 *
 * @author Herbert Caffarel
 */
public class HangmanPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected final HangmanFrame frame; // La fenêtre parente
    protected final BorderLayout layout; // gestionnaire de placement
    protected final String title; // Le titre de la page
    protected final JLabel titleLabel; // Le label d'affichage du titre
    protected Font titleFont; // La fonte personnalisée

    public HangmanPanel(String title, HangmanFrame frame) {
        this.frame = frame;
        this.title = title;
        layout = new BorderLayout();
        // Récupération de la fonte personnalisée
        titleFont = new Font("Retro Flower", Font.PLAIN, 96);
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        // Organisation du contenu visuel
        initGui();
    }

    /**
     * Organisation des ojets graphiques dans le panneau.
     */
    private void initGui() {
        setLayout(layout);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 10),
                BorderFactory.createEmptyBorder(15, 15, 15, 15))
        );
        if (titleFont != null) {
            titleLabel.setFont(titleFont);
        }
        add(titleLabel, BorderLayout.NORTH);
    }

}

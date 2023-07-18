package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Herbert Caffarel
 */
public class HangmanPanel extends JPanel {

    protected final BorderLayout layout;
    protected final String title;
    protected final JLabel titleLabel;
    protected Font titleFont;

    public HangmanPanel(String title) {
        this.title = title;
        layout = new BorderLayout();
        try {
            InputStream stream = HangmanPanel.class.getResourceAsStream("/fonts/retro-flower.regular.otf");
            titleFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(96f);
        } catch (FontFormatException | IOException ex) {
            titleFont = null;
        }
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        initGui();
    }

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

package gui;

import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Herbert Caffarel
 */
public class CenteredGameLabel extends JPanel {

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

    public void setText(String text) {
        label.setText(text);
    }

    void setIcon(Icon icon) {
        label.setIcon(icon);
    }

}

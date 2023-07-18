package gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Herbert Caffarel
 */
public class HangmanPanel extends JPanel {

    protected final BorderLayout layout;
    protected final String title;
    protected final JLabel titleLabel;

    public HangmanPanel(String title) {
        this.title = title;
        layout = new BorderLayout();
        titleLabel = new JLabel(title);
        initGui();
    }

    private void initGui() {
        setLayout(layout);
        add(titleLabel, BorderLayout.NORTH);
    }

}

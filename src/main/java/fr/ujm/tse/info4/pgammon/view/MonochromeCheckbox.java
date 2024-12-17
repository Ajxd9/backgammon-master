package fr.ujm.tse.info4.pgammon.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

/**
 * Checkbox component with extended design.
 * Author: Jean-Mi
 */
public class MonochromeCheckbox extends JCheckBox {
    private static final long serialVersionUID = -1079113884826936210L;
    public static final String WHITE = "WHITE";
    public static final String BLACK = "BLACK";

    private String color;
    private ImageIcon icon;
    private JLabel label;
    private String text;

    /**
     * New unselected checkbox.
     * @param label text associated with the checkbox.
     */
    public MonochromeCheckbox(String label) {
        color = BLACK;
        text = label;

        build();
    }

    private void build() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(200, 50));
        icon = new ImageIcon("images/check.png");
        label = new JLabel(text);
        label.setForeground(new Color(0xCCCCCC));
        label.setBounds(40, 0, getPreferredSize().width - 40, getPreferredSize().height);
        add(label);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int height = getHeight();

        if (!model.isEnabled()) {
            label.setForeground(new Color(0x555555));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        } else {
            label.setForeground(new Color(0xCCCCCC));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        Paint paint;

        // Background
        if (model.isRollover())
            paint = (color.equals(WHITE)) ? new Color(0xAAAAAA) : new Color(0x666666);
        else
            paint = (color.equals(WHITE)) ? new Color(0xCCCCCC) : new Color(0x888888);

        g2.setPaint(paint);
        g2.fillRect(4, (height - 26) / 2, 26, 26);

        // Border
        if (model.isPressed())
            paint = (color.equals(WHITE)) ? new Color(0x777777) : new Color(0x333333);
        else
            paint = (color.equals(WHITE)) ? new Color(0x888888) : new Color(0x444444);
        g2.setStroke(new BasicStroke(3.0f));
        g2.setPaint(paint);
        g2.drawRect(4, (height - 26) / 2, 26, 26);

        // Icon
        if (isSelected())
            g2.drawImage(icon.getImage(), 4, (height - 26) / 2, this);

        g2.dispose();
    }
}

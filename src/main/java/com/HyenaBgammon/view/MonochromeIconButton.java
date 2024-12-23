package com.HyenaBgammon.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Square button containing an image.
 * Author: Jean-Mi
 */
public class MonochromeIconButton extends JButton {
    private static final long serialVersionUID = -8152217751563954978L;

    public enum Size {
        SMALL,
        BIG
    }

    public static final String WHITE = "WHITE";
    public static final String BLACK = "BLACK";

    private String color;
    private ImageIcon icon;
    @SuppressWarnings("unused")
    private JLabel label;
    private Size size;

    /**
     * Creates a new button.
     * @param iconRef path to the icon to use (see MonochromeIconType).
     * @param text Not implemented yet.
     * @param color MonochromeIconButton.WHITE or MonochromeIconButton.BLACK.
     */
    public MonochromeIconButton(String iconRef, String text, String color) {
        size = Size.SMALL;
        this.color = color;

        build(iconRef, text);
    }

    /**
     * Creates a new button of type WHITE.
     * @param iconRef path to the icon to use (see MonochromeIconType).
     * @param text Not implemented yet.
     */
    public MonochromeIconButton(String iconRef, String text) {
        size = Size.SMALL;
        color = WHITE;
        build(iconRef, text);
    }

    private void build(String iconRef, String text) {
        setOpaque(false);
        icon = new ImageIcon(iconRef);
        label = new JLabel(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!isEnabled())
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

        int height = getHeight();
        int width = getWidth();

        Paint paint;

        // Background
        if (!model.isEnabled()) {
            setForeground(Color.GRAY);
            paint = new Color(0x444444);
        } else {
            if (model.isRollover())
                paint = (color.equals(WHITE)) ? new Color(0xAAAAAA) : new Color(0x666666);
            else
                paint = (color.equals(WHITE)) ? new Color(0xCCCCCC) : new Color(0x888888);
        }
        g2.setPaint(paint);
        g2.fillRect(0, 0, width, height);

        // Border
        if (model.isPressed())
            paint = (color.equals(WHITE)) ? new Color(0x777777) : new Color(0x333333);
        else
            paint = (color.equals(WHITE)) ? new Color(0x888888) : new Color(0x444444);
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(paint);
        g2.drawRect(0, 0, width - 1, height - 1);

        // Icon
        int offsetX = (size == Size.BIG) ? 3 : -1;
        int offsetY = (size == Size.BIG) ? 3 : -1;
        g2.drawImage(icon.getImage(), offsetX, offsetY, this);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // super.paintBorder(g);
    }

    /**
     * Sets the button to small size.
     */
    public void setSizeSmall() {
        setPreferredSize(new Dimension(40, 40));
        size = Size.SMALL;
    }

    /**
     * Sets the button to big size.
     */
    public void setSizeBig() {
        setPreferredSize(new Dimension(60, 60));
        size = Size.BIG;
    }
}

package fr.ujm.tse.info4.pgammon.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Button with extended design.
 * Author: Jean-Mi
 */
public class MonochromeButton extends JButton {
    
    private static final long serialVersionUID = 1L;
    String text;
    JLabel label;
    float alpha = 1f;
    
    /**
     * Creates a new empty button.
     */
    public MonochromeButton() {
        super();
    }

    /**
     * Creates a new button with text.
     * @param str : the button label.
     */
    public MonochromeButton(String str) {
        super();
        text = str;
        build();
    }

    /**
     * Creates a new button with text.
     * @param text : the button label.
     * @param icon : Not implemented yet.
     */
    public MonochromeButton(String text, Icon icon) {
        super();
        this.text = text;
        build();
    }

    private void build() {
        setOpaque(false);
        label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setPreferredSize(getPreferredSize());
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setForeground(new Color(0xF2F2F2));
        add(label);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        label.setPreferredSize(preferredSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float alphaCoef = 1;
        if (!isEnabled()) 
            alphaCoef = 0.3f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * alphaCoef));

        int height = getHeight(); 
        int width = getWidth(); 
        
        Paint paint;
        ButtonModel model = getModel();

        // Background
        if (!model.isEnabled()) { 
            setForeground(Color.GRAY);
            paint = new Color(0x444444);
        } else { 
            if (model.isRollover()) { 
                paint = new Color(0x333333);
            } else { 
                paint = new Color(0x222222);
            } 
        } 
        g2.setPaint(paint); 
        g2.fillRect(0, 0, width, height);

        // Border
        if (model.isPressed()) { 
            paint = new Color(0x555555);
        } else { 
            paint = new Color(0x888888);
        } 
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(paint); 
        g2.drawRect(1, 1, width - 3, height - 3);

        g2.dispose(); 
    }

    @Override
    protected void paintBorder(Graphics g) {
        // super.paintBorder(g);
    }

    /**
     * Changes the button's text.
     */
    @Override
    public void setText(String text) {
        this.text = text;
        label.setText(text);
    }

    /**
     * Changes the button's transparency.
     * @param value : transparency value between 0.0f and 1.0f.
     */
    public void setAlpha(float value) {
        alpha = value;
    }
}

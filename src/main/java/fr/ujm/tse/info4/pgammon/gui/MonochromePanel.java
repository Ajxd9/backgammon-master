package fr.ujm.tse.info4.pgammon.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Base panel displaying a title and a black frame.
 * This panel has a null layout.
 * Author: Jean-Michel
 */
public class MonochromePanel extends JButton {

    public static final int TITLE_HEIGHT = 30;

    private static final long serialVersionUID = 1L;
    private String title = "";

    private JLabel titleLabel;
    private JPanel pane;

    /**
     * Constructs a MonochromePanel with the title provided as a parameter.
     * @param title Title of the window.
     */
    public MonochromePanel(String title) {
        super();
        this.title = title;
        build();
    }

    /**
     * Constructs a MonochromePanel without a title.
     * A space is still reserved for the title.
     */
    public MonochromePanel() {
        super();
        build();
    }

    /**
     * Changes the title of the panel.
     * @param title New title.
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
        this.title = title;
    }

    private void build() {
        setOpaque(false);
        setLayout(null);
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0xB3B3B3));
        titleLabel.setAlignmentX(0);
        titleLabel.setBounds(0, 0, 400, TITLE_HEIGHT);
        titleLabel.repaint();

        pane = new JPanel();
        pane.setBounds(0, TITLE_HEIGHT, getWidth(), getHeight() - TITLE_HEIGHT);
        pane.repaint();

        add(titleLabel);
        add(pane);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint paint;
        int height = getHeight();
        int width = getWidth();

        // Background
        paint = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
                getHeight() / 2.0),
                getHeight(),
                new float[]{0.0f, 0.8f},
                new Color[]{new Color(0x333333), new Color(0x000000)},
                RadialGradientPaint.CycleMethod.NO_CYCLE);

        g2.setPaint(paint);
        g2.fillRect(0, TITLE_HEIGHT, width, height - TITLE_HEIGHT);

        // Border
        paint = new Color(0x808080);
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(paint);
        g2.drawRect(2, TITLE_HEIGHT, width - 5, height - 5 - TITLE_HEIGHT);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // super.paintBorder(g);
    }
}

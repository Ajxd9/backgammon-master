package fr.ujm.tse.info4.pgammon.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ujm.tse.info4.pgammon.models.Clock;
import fr.ujm.tse.info4.pgammon.models.ClockEvent;
import fr.ujm.tse.info4.pgammon.models.ClockEventListener; // change according to model

/**
 * Provides a component to display the clock as a bar.
 * Author: Jean-Mi
 */
public class ClockBar extends JPanel implements ClockEventListener {
    private static final long serialVersionUID = 1946775586045691887L;
    private final int LABEL_WIDTH = 50;
    JLabel timeLabel;
    Clock clock;

    /**
     * Displays the clock in a bar.
     * @param clock Associated clock.
     */
    public ClockBar(Clock clock) {
        setClock(clock);
        build();
    }

    /**
     * Sets the clock.
     * @param clock Clock to associate.
     */
    public void setClock(Clock clock) {
        if (this.clock != null)
            this.clock.removeListener(this);
        this.clock = clock;
        if (this.clock != null)
            this.clock.addListener(this);

        setVisible(this.clock != null);
    }

    private void build() {
        setLayout(null);
        setOpaque(false);
        timeLabel = new JLabel("");
        timeLabel.setForeground(new Color(0xCCCCCC));
        timeLabel.setBounds(10, 0, LABEL_WIDTH - 10, 20);
        add(timeLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (clock == null)
            return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint paint;
        int height = getHeight();
        int width = getWidth();
        int timeWidth = (width - LABEL_WIDTH) - (int) ((width - LABEL_WIDTH) * clock.getRatio());

        // Background
        paint = new Color(0xDDDDDD);
        g2.setPaint(paint);
        g2.fillRect(LABEL_WIDTH + 6, 6, timeWidth - 9, height - 11);

        // Border
        paint = new Color(0xAAAAAA);
        g2.setStroke(new BasicStroke(3.0f));
        g2.setPaint(paint);
        g2.drawRect(2 + LABEL_WIDTH, 2, width - 4 - LABEL_WIDTH, height - 4);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {}

    @Override
    public void clockEnd(ClockEvent clockEvent) {}

    @Override
    public void updateClock(ClockEvent evt) {
        if (evt.getSource() == clock)
            repaint();

        if (timeLabel != null)
            timeLabel.setText(clock.getRemainingTime());
    }
}

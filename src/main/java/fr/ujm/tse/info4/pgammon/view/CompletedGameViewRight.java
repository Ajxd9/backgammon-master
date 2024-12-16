package fr.ujm.tse.info4.pgammon.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ujm.tse.info4.pgammon.gui.IconMonochromeType;
import fr.ujm.tse.info4.pgammon.gui.MonochromeIconButton;

public class CompletedGameViewRight extends JPanel {
    /**
     * This class manages the right view at the end of a game.
     * It includes options to review the game and start a new one.
     */

    private static final long serialVersionUID = 1735758716220950070L;

    // Components for the right panel
    private MonochromeIconButton nextButton;
    private MonochromeIconButton replayButton;
    private MonochromeIconButton interruptButton;
    private JLabel nextLabel;

    /**
     * Constructor for the class.
     */
    public CompletedGameViewRight() {
        build();
    }

    private void build() {
        setLayout(null);
        setOpaque(false);

        // Right panel setup

        // Button to go to the next game
        nextButton = new MonochromeIconButton(IconMonochromeType.NEXT, "MonochromeIconButton", MonochromeIconButton.BLACK);
        nextButton.setSizeBig();
        nextButton.setBounds(10, 20, nextButton.getPreferredSize().width, nextButton.getPreferredSize().height);
        add(nextButton);

        nextLabel = new JLabel("<html>Next<br>Game");
        nextLabel.setForeground(new Color(0xCCCCCC));
        nextLabel.setBounds(15, 70, 80, 60);
        add(nextLabel);

        // Button to replay the game
        replayButton = new MonochromeIconButton(IconMonochromeType.PLAY, "MonochromeIconButton", MonochromeIconButton.BLACK);
        replayButton.setSizeBig();
        replayButton.setBounds(10, 185, replayButton.getPreferredSize().width, replayButton.getPreferredSize().height);
        add(replayButton);

        JLabel replayLabel = new JLabel("Replay!");
        replayLabel.setForeground(new Color(0xCCCCCC));
        replayLabel.setBounds(10, 240, 80, 60);
        add(replayLabel);

        // Button to interrupt the game
        interruptButton = new MonochromeIconButton(IconMonochromeType.INTERRUPT_WHITE, "MonochromeIconButton", MonochromeIconButton.BLACK);
        interruptButton.setSizeBig();
        interruptButton.setBounds(10, 350, interruptButton.getPreferredSize().width, interruptButton.getPreferredSize().height);
        add(interruptButton);

        JLabel interruptLabel = new JLabel("<html>Interrupt<br>Game");
        interruptLabel.setForeground(new Color(0xCCCCCC));
        interruptLabel.setBounds(10, 400, 80, 60);
        add(interruptLabel);
    }

    /**
     * Getter for the next button.
     * @return the next button component.
     */
    public MonochromeIconButton getNextButton() {
        return nextButton;
    }

    /**
     * Getter for the replay button.
     * @return the replay button component.
     */
    public MonochromeIconButton getReplayButton() {
        return replayButton;
    }

    /**
     * Getter for the interrupt button.
     * @return the interrupt button component.
     */
    public MonochromeIconButton getInterruptButton() {
        return interruptButton;
    }

    /**
     * Getter for the next label.
     * @return the label for the next button.
     */
    public JLabel getNextLabel() {
        return nextLabel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Paint gradientPaint;
        int height = getHeight();
        int width = getWidth();

        // Background gradient
        gradientPaint = new RadialGradientPaint(
            new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0),
            getHeight(),
            new float[] { 0.0f, 0.8f },
            new Color[] { new Color(0x333333), new Color(0x000000) },
            RadialGradientPaint.CycleMethod.NO_CYCLE
        );

        g2.setPaint(gradientPaint);
        g2.fillRect(0, 0, width, height);
        g2.dispose();
    }
}

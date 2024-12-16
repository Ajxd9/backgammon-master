package fr.ujm.tse.info4.pgammon.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;

import fr.ujm.tse.info4.pgammon.models.Square;
import fr.ujm.tse.info4.pgammon.models.SquareColor;

public class BarCaseButton extends CaseButton {
    private static final long serialVersionUID = 1696544283522096083L;
    private final int MAX_CHECKERS_DRAWN = 5;
    private final int CHECKER_SEPARATION = 27;

    public BarCaseButton(Square _case, boolean _isDirectionUp) {
        super(_case);
        build();
    }

    private void build() {
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(33, 150));
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawBackground(g);
        drawCheckers(g);
    }

    private void drawBackground(Graphics g) {
        if (isPossible() && getCase().getNumCheckers() == 0) {
            g.setColor(new Color(0x000033));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void drawCheckers(Graphics g) {
    	Square c = getCase();
        if (c == null)
            return;

        int h = getHeight();
        SquareColor checkerColor = c.getCheckerColor();

        if (checkerColor == SquareColor.EMPTY || c.getNumCheckers() == 0)
            return;

        int checkerCount = c.getNumCheckers();
        int count = Math.min(checkerCount, MAX_CHECKERS_DRAWN);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offset = 0;
        if (model.isRollover())
            offset++;
        if (model.isPressed())
            offset++;

        ImageIcon icon;
        if (getCase().getCheckerColor() == SquareColor.EMPTY) {
            icon = new ImageIcon();
        } else if (isPossible()) {
            icon = (getCase().getCheckerColor() == SquareColor.WHITE) ? whiteAssistIcon : blackAssistIcon;
        } else {
            icon = (getCase().getCheckerColor() == SquareColor.WHITE) ? whiteIcon : blackIcon;
        }

        int selectedCount = 0;
        if (isCandidate())
            selectedCount = 1;

        for (int i = 0; i < count - selectedCount; i++) {
            int y = (int) ((h - CHECKER_SEPARATION) / 2 + (i - (count - 1) / 2f) * (CHECKER_SEPARATION) + offset);
            g2.drawImage(icon.getImage(), 0, y, this);
        }

        if (isCandidate() && c.getNumCheckers() > 0) {
            float i = count - 0.8f;
            int y = (int) ((h - CHECKER_SEPARATION) / 2 + (i - (count - 1) / 2f) * (CHECKER_SEPARATION) + offset);
            ImageIcon transparentIcon = (getCase().getCheckerColor() == SquareColor.WHITE) ? whiteTransparentIcon : blackTransparentIcon;

            g2.drawImage(transparentIcon.getImage(), 1, y, this);
        }

        if (checkerCount > MAX_CHECKERS_DRAWN) {
            String checkerCountString = Integer.toString(checkerCount);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            if (c.getCheckerColor() == SquareColor.WHITE)
                g2.setColor(new Color(0x111111));
            else
                g2.setColor(new Color(0xCCCCCC));
            int y = (h / 2) + 6;

            g2.drawChars(checkerCountString.toCharArray(), 0, checkerCountString.length(), 11 - (checkerCountString.length() - 1) * 5, y);
        }

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // super.paintBorder(g);
    }
}

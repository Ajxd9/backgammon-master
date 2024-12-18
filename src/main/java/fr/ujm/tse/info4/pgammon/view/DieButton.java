package fr.ujm.tse.info4.pgammon.view;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import fr.ujm.tse.info4.pgammon.models.SquareColor;  //change according to model 
import fr.ujm.tse.info4.pgammon.models.SixSidedDie;

public class DieButton extends JButton {
    private static final long serialVersionUID = 2520612785614004478L;

    // White Regular Dice
    private static final String WHITE_DIE_1 = "images/des/de_blanc_1.png";
    private static final String WHITE_DIE_2 = "images/des/de_blanc_2.png";
    private static final String WHITE_DIE_3 = "images/des/de_blanc_3.png";
    private static final String WHITE_DIE_4 = "images/des/de_blanc_4.png";
    private static final String WHITE_DIE_5 = "images/des/de_blanc_5.png";
    private static final String WHITE_DIE_6 = "images/des/de_blanc_6.png";

    // White Enhanced Dice (-1, -2, -3)
    private static final String WHITE_DIE_1M = "images/des/de_blanc_-1.png";
    private static final String WHITE_DIE_2M = "images/des/de_blanc_-2.png";
    private static final String WHITE_DIE_3M = "images/des/de_blanc_-3.png";

    // White Question Dice
    private static final String WHITE_DIE_Easy = "images/des/de_blanc_easy.png";
    private static final String WHITE_DIE_Medium = "images/des/de_blanc_medium.png";
    private static final String WHITE_DIE_Hard = "images/des/de_blanc_hard.png";

    // Black Regular Dice
    private static final String BLACK_DIE_1 = "images/des/de_noir_1.png";
    private static final String BLACK_DIE_2 = "images/des/de_noir_2.png";
    private static final String BLACK_DIE_3 = "images/des/de_noir_3.png";
    private static final String BLACK_DIE_4 = "images/des/de_noir_4.png";
    private static final String BLACK_DIE_5 = "images/des/de_noir_5.png";
    private static final String BLACK_DIE_6 = "images/des/de_noir_6.png";

    // Black Enhanced Dice (-1, -2, -3)
    private static final String BLACK_DIE_1M = "images/des/de_noir_-1.png";
    private static final String BLACK_DIE_2M = "images/des/de_noir_-2.png";
    private static final String BLACK_DIE_3M = "images/des/de_noir_-3.png";

    // Black Question Dice
    private static final String BLACK_DIE_Easy = "images/des/de_noir_easy.png";
    private static final String BLACK_DIE_Medium = "images/des/de_noir_medium.png";
    private static final String BLACK_DIE_Hard = "images/des/de_noir_hard.png";

    private SixSidedDie die;
    private ImageIcon icon;

    public DieButton(SixSidedDie die) {
        this.die = die;
        setEnabled(false);
        setOpaque(false);
        setPreferredSize(new Dimension(32, 32));
        update();
    }

    public SixSidedDie getDie() {
        return die;
    }

    public void setDie(SixSidedDie die) {
        this.die = die;
        update();
    }

    private void update() {
        String iconRef = WHITE_DIE_1; // Default icon

        if (die.getDieColor() == SquareColor.WHITE) {
            switch (die.getDieType()) {
                case REGULAR:
                    iconRef = getRegularIcon(true, die.getValue());
                    break;
                case QUESTION:
                    iconRef = getQuestionIcon(true, die.getValue());
                    break;
                case ENHANCED:
                    iconRef = getEnhancedIcon(true, die.getValue());
                    break;
            }
        } else {
            switch (die.getDieType()) {
                case REGULAR:
                    iconRef = getRegularIcon(false, die.getValue());
                    break;
                case QUESTION:
                    iconRef = getQuestionIcon(false, die.getValue());
                    break;
                case ENHANCED:
                    iconRef = getEnhancedIcon(false, die.getValue());
                    break;
            }
        }

        icon = new ImageIcon(iconRef);
        updateUI();
    }

    private String getRegularIcon(boolean isWhite, int value) {
        if (isWhite) {
            switch (value) {
                case 1: return WHITE_DIE_1;
                case 2: return WHITE_DIE_2;
                case 3: return WHITE_DIE_3;
                case 4: return WHITE_DIE_4;
                case 5: return WHITE_DIE_5;
                case 6: return WHITE_DIE_6;
            }
        } else {
            switch (value) {
                case 1: return BLACK_DIE_1;
                case 2: return BLACK_DIE_2;
                case 3: return BLACK_DIE_3;
                case 4: return BLACK_DIE_4;
                case 5: return BLACK_DIE_5;
                case 6: return BLACK_DIE_6;
            }
        }
        return "";
    }

    private String getQuestionIcon(boolean isWhite, int value) {
        if (isWhite) {
            switch (value) {
                case 1: return WHITE_DIE_Easy;
                case 2: return WHITE_DIE_Medium;
                case 3: return WHITE_DIE_Hard;
            }
        } else {
            switch (value) {
                case 1: return BLACK_DIE_Easy;
                case 2: return BLACK_DIE_Medium;
                case 3: return BLACK_DIE_Hard;
            }
        }
        return "";
    }

    private String getEnhancedIcon(boolean isWhite, int value) {
        if (isWhite) {
            switch (value) {
                case -1: return WHITE_DIE_1M;
                case -2: return WHITE_DIE_2M;
                case -3: return WHITE_DIE_3M;
            }
        } else {
            switch (value) {
                case -1: return BLACK_DIE_1M;
                case -2: return BLACK_DIE_2M;
                case -3: return BLACK_DIE_3M;
            }
        }
        return "";
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (die.isUsed())
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g2.drawImage(icon.getImage(), 0, 0, this);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Empty to remove border rendering
    }
}

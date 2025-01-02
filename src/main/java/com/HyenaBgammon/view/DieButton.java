package com.HyenaBgammon.view;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.HyenaBgammon.models.DieType;
import com.HyenaBgammon.models.SixSidedDie;
import com.HyenaBgammon.models.SquareColor;

public class DieButton extends JButton {
    private static final long serialVersionUID = 2520612785614004478L;
    
    /*Normal dices*/
    private static final String WHITE_DIE_1 = "images/des/de_blanc_1.png";
    private static final String WHITE_DIE_2 = "images/des/de_blanc_2.png";
    private static final String WHITE_DIE_3 = "images/des/de_blanc_3.png";
    private static final String WHITE_DIE_4 = "images/des/de_blanc_4.png";
    private static final String WHITE_DIE_5 = "images/des/de_blanc_5.png";
    private static final String WHITE_DIE_6 = "images/des/de_blanc_6.png";
    private static final String BLACK_DIE_1 = "images/des/de_noir_1.png";
    private static final String BLACK_DIE_2 = "images/des/de_noir_2.png";
    private static final String BLACK_DIE_3 = "images/des/de_noir_3.png";
    private static final String BLACK_DIE_4 = "images/des/de_noir_4.png";
    private static final String BLACK_DIE_5 = "images/des/de_noir_5.png";
    private static final String BLACK_DIE_6 = "images/des/de_noir_6.png";
    
    /*Advanced dices*/
    private static final String WHITE_DIE_0 = "images/des/0.png";
    private static final String WHITE_DIE_Min1 = "images/des/de_blanc_-1.png";
    private static final String WHITE_DIE_Min2 = "images/des/de_blanc_-2.png";
    private static final String WHITE_DIE_Min3 = "images/des/de_blanc_-3.png";
    private static final String BLACK_DIE_0 = "images/des/de_noir_0.png";
    private static final String BLACK_DIE_Min1 = "images/des/de_noir_-1.png";
    private static final String BLACK_DIE_Min2 = "images/des/de_noir_-2.png";
    private static final String BLACK_DIE_Min3 = "images/des/de_noir_-3.png";
    
    /*Difficulty dices*/
    private static final String Easy_DIE = "images/des/Easy.png";
    private static final String Medium_DIE = "images/des/Med.png";
    private static final String Hard_DIE = "images/des/Hard.png";

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
        String iconRef = WHITE_DIE_1; // Default icon for white dice with value 1

        // Handle QUESTION dice
        if (die.getDieType() == DieType.QUESTION) {
            switch (die.getValue()) {
                case 1:
                    iconRef = Easy_DIE; // Easy question
                    break;
                case 2:
                    iconRef = Medium_DIE; // Medium question
                    break;
                case 3:
                    iconRef = Hard_DIE; // Hard question
                    break;
            }
        } 
        // Handle WHITE dice (regular and enhanced)
        else if (die.getDieColor() == SquareColor.WHITE) {
            switch (die.getValue()) {
                case -3:
                    iconRef = WHITE_DIE_Min3;
                    break;
                case -2:
                    iconRef = WHITE_DIE_Min2;
                    break;
                case -1:
                    iconRef = WHITE_DIE_Min1;
                    break;
                case 0:
                    iconRef = WHITE_DIE_0;
                    break;
                case 1:
                    iconRef = WHITE_DIE_1;
                    break;
                case 2:
                    iconRef = WHITE_DIE_2;
                    break;
                case 3:
                    iconRef = WHITE_DIE_3;
                    break;
                case 4:
                    iconRef = WHITE_DIE_4;
                    break;
                case 5:
                    iconRef = WHITE_DIE_5;
                    break;
                case 6:
                    iconRef = WHITE_DIE_6;
                    break;
            }
        } 
        // Handle BLACK dice (regular and enhanced)
        else if (die.getDieColor() == SquareColor.BLACK) {
            switch (die.getValue()) {
                case -3:
                    iconRef = BLACK_DIE_Min3;
                    break;
                case -2:
                    iconRef = BLACK_DIE_Min2;
                    break;
                case -1:
                    iconRef = BLACK_DIE_Min1;
                    break;
                case 0:
                    iconRef = BLACK_DIE_0;
                    break;
                case 1:
                    iconRef = BLACK_DIE_1;
                    break;
                case 2:
                    iconRef = BLACK_DIE_2;
                    break;
                case 3:
                    iconRef = BLACK_DIE_3;
                    break;
                case 4:
                    iconRef = BLACK_DIE_4;
                    break;
                case 5:
                    iconRef = BLACK_DIE_5;
                    break;
                case 6:
                    iconRef = BLACK_DIE_6;
                    break;
            }
        }

        // Update the button's icon based on the resolved reference
        icon = new ImageIcon(iconRef);
        updateUI(); // Trigger a UI refresh
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (die.isUsed())
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        // Icon
        g2.drawImage(icon.getImage(), 0, 0, this);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // super.paintBorder(g);
    }
}

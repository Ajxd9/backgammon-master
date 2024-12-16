package fr.ujm.tse.info4.pgammon.gui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import fr.ujm.tse.info4.pgammon.models.CouleurCase;  //change according to model 
import fr.ujm.tse.info4.pgammon.models.DeSixFaces;

public class DieButton extends JButton {
    private static final long serialVersionUID = 2520612785614004478L;
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

    private DeSixFaces die;
    private ImageIcon icon;

    public DieButton(DeSixFaces die) {
        this.die = die;
        setEnabled(false);
        setOpaque(false);
        setPreferredSize(new Dimension(32, 32));
        update();
    }

    public DeSixFaces getDie() {
        return die;
    }

    public void setDie(DeSixFaces die) {
        this.die = die;
        update();
    }

    private void update() {
        String iconRef = WHITE_DIE_1;
        if (die.getDieColor() == CellColor.WHITE) {
            switch (die.getValue()) {
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
        } else {
            switch (die.getValue()) {
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
        icon = new ImageIcon(iconRef);
        updateUI();
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

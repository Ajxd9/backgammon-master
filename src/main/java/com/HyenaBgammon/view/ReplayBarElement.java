package com.HyenaBgammon.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.HyenaBgammon.models.SquareColor;
import com.HyenaBgammon.models.SixSidedDie;
import com.HyenaBgammon.models.Movement;

public class ReplayBarElement extends JPanel {
    private static final long serialVersionUID = -6139180165139138217L;
    private SquareColor color;
    private SixSidedDie die;
    private Movement movement;

    public static final ImageIcon takenBlack = new ImageIcon("images/defaite_pion_noir.png");
    public static final ImageIcon takenWhite = new ImageIcon("images/defaite_pion_blanc.png");
    public static final ImageIcon enteredBlack = new ImageIcon("images/victoire_pion_noir.png");
    public static final ImageIcon enteredWhite = new ImageIcon("images/victoire_pion_blanc.png");
    public static final ImageIcon impossibleBlack = new ImageIcon("images/coup_impossible_noir.png");
    public static final ImageIcon impossibleWhite = new ImageIcon("images/coup_impossible_blanc.png");

    public ReplayBarElement(SixSidedDie die, Movement movement) {
        super();
        this.movement = movement;
        this.color = die.getDieColor();
        this.die = die;

        build();
    }

    private void build() {
        setPreferredSize(new Dimension(30, 100));
        setLayout(null);

        DieButton dieButton = new DieButton(die);
        dieButton.setBounds(-1, 0, 
                             dieButton.getPreferredSize().width, dieButton.getPreferredSize().height);
        add(dieButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int height = getHeight();
        int width = getWidth();

        Paint paint;

        // Background
        paint = (color == SquareColor.WHITE) ? new Color(0xEEEEEE) : new Color(0x111111);
        g2.setPaint(paint);
        g2.fillRect(0, 0, width, height);

        ImageIcon icon = new ImageIcon();

        if (movement == null) {
            icon = (color == SquareColor.WHITE) ? impossibleWhite : impossibleBlack;
        } else {
            if (movement.isBearOffMove()) {
                icon = (color == SquareColor.WHITE) ? enteredWhite : enteredBlack;
            } else if (movement.isHitMove()) {
                icon = (color == SquareColor.WHITE) ? takenWhite : takenBlack;
            }
        }

        g2.drawImage(icon.getImage(), 0, 60, this);
        g2.dispose();
    }
}

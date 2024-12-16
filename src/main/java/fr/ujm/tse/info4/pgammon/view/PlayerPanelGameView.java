package fr.ujm.tse.info4.pgammon.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ujm.tse.info4.pgammon.gui.ImageAvatar;
import fr.ujm.tse.info4.pgammon.gui.MonochromeCheckbox;
import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.Player;
import fr.ujm.tse.info4.pgammon.models.AssistantLevel;
import fr.ujm.tse.info4.pgammon.models.SquareColor;

public class PlayerPanelGameView extends JPanel {

    /**
     * This class represents a player panel in the game view.
     */
    private static final long serialVersionUID = -7344853213808679707L;

    private Player player;
    private SquareColor color;

    public static final String whiteChecker = "images/big_pion_blanc.png";
    public static final String blackChecker = "images/big_pion_noir.png";
    private ImageAvatar playerImage;
    private ImageIcon checkerImage;

    private MonochromeCheckbox showPossibleMovesCheckbox;
    private MonochromeCheckbox suggestNextMoveCheckbox;
    private JLabel playerNameLabel;

    /**
     * Constructor for the class
     * @param p Player passed as a parameter
     * @param col SquareColor passed as a parameter
     */
    public PlayerPanelGameView(Player p, SquareColor col) {
        player = p;
        color = col;

        build();
        updateData();
    }
    
    /**
     * Updates the player panel.
     */
    public void updateData() {
        if (player.getAssistantLevel() == AssistantLevel.FULL) {
            showPossibleMovesCheckbox.setSelected(true);
            suggestNextMoveCheckbox.setSelected(true);
        } else if (player.getAssistantLevel() == AssistantLevel.BASIC) {
            showPossibleMovesCheckbox.setSelected(true);
            suggestNextMoveCheckbox.setSelected(false);
        } else {
            showPossibleMovesCheckbox.setSelected(false);
            suggestNextMoveCheckbox.setSelected(false);
        }
        if (showPossibleMovesCheckbox.isSelected()) {
            suggestNextMoveCheckbox.setEnabled(true);
        } else {
            suggestNextMoveCheckbox.setEnabled(false);
        }
    }

    public void build() {
        setLayout(null);

        // Retrieve the image
        try {
            if (color.equals(SquareColor.WHITE)) {
                checkerImage = new ImageIcon(whiteChecker);
            } else {
                checkerImage = new ImageIcon(blackChecker);
            }

        } catch (Exception err) {
            System.err.println(err);
        }
        
        playerImage = new ImageAvatar(player.getImageSource());
        playerImage.setBounds(15, 40, 50, 50);
        add(playerImage);

        playerNameLabel = new JLabel();
        showPossibleMovesCheckbox = new MonochromeCheckbox("<html> Show <br> possible moves");
        suggestNextMoveCheckbox = new MonochromeCheckbox("<html> Suggest <br> next move");
        
        // Initially hide the suggestion checkbox since it is not yet implemented
        suggestNextMoveCheckbox.setVisible(false);

        JPanel nameCenteringPanel = new JPanel();
        nameCenteringPanel.setLayout(new BorderLayout());
        nameCenteringPanel.setBounds(0, 0, 150, 50);
        nameCenteringPanel.setOpaque(false);

        // Display the player's stats
        playerNameLabel.setForeground(new Color(0xCCCCCC));
        playerNameLabel.setText(player.getUsername());
        playerNameLabel.setHorizontalAlignment(JLabel.CENTER);

        add(nameCenteringPanel);
        nameCenteringPanel.add(playerNameLabel);

        // Create components for the checkboxes
        showPossibleMovesCheckbox.setForeground(new Color(0xCCCCCC));
        showPossibleMovesCheckbox.setBounds(10, 120, 150, 50);
        showPossibleMovesCheckbox.setOpaque(false);

        suggestNextMoveCheckbox.setForeground(new Color(0xCCCCCC));
        suggestNextMoveCheckbox.setBounds(10, 160, 150, 50);
        suggestNextMoveCheckbox.setOpaque(false);

        // Add components
        add(showPossibleMovesCheckbox);
        add(suggestNextMoveCheckbox);
    }
    
    /**
     * Getter for the "Show possible moves" checkbox.
     * @return returns the checkbox.
     */
    public MonochromeCheckbox getShowPossibleMovesCheckbox() {
        return showPossibleMovesCheckbox;
    }

    /**
     * Setter for the "Show possible moves" checkbox.
     * @param showPossibleMovesCheckbox updates the checkbox value.
     */
    public void setShowPossibleMovesCheckbox(MonochromeCheckbox showPossibleMovesCheckbox) {
        this.showPossibleMovesCheckbox = showPossibleMovesCheckbox;
    }

    /**
     * Getter for the "Suggest next move" checkbox.
     * @return returns the checkbox.
     */
    public MonochromeCheckbox getSuggestNextMoveCheckbox() {
        return suggestNextMoveCheckbox;
    }

    /**
     * Setter for the "Suggest next move" checkbox.
     * @param suggestNextMoveCheckbox updates the checkbox value.
     */
    public void setSuggestNextMoveCheckbox(MonochromeCheckbox suggestNextMoveCheckbox) {
        this.suggestNextMoveCheckbox = suggestNextMoveCheckbox;
    }

    /**
     * Getter for the player's name label.
     * @return returns the player's name label.
     */
    public JLabel getPlayerNameLabel() {
        return playerNameLabel;
    }

    public void setPlayerNameLabel(JLabel playerNameLabel) {
        this.playerNameLabel = playerNameLabel;
    }

    /**
     * Getter for the player.
     * @return returns the player.
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create(); 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 

        Paint p;
        int h = getHeight(); 
        int w = getWidth(); 

        // Background
        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0,
                getHeight() / 2.0), 
                getHeight(),
                new float[] { 0.0f, 0.8f },
                new Color[] { new Color(0x333333), new Color(0x000000) },
                RadialGradientPaint.CycleMethod.NO_CYCLE);

        g2.setPaint(p); 
        g2.fillRect(0, 0, w, h); 

        // Border
        p = new Color(0x808080);
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(p); 
        g2.drawRect(2, 0, w - 5 , h - 5);

        g.drawImage(checkerImage.getImage(), 80, 55, this);
        g2.dispose(); 
    }

}

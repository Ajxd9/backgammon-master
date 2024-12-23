package com.HyenaBgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import com.HyenaBgammon.models.GameDifficulty;
import com.HyenaBgammon.models.Player;
import com.HyenaBgammon.models.SquareColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;


public class NewSessionView extends JPanel {

    /**
     * This class represents the view to create a new game session.
     */
    private static final long serialVersionUID = -5590865478480555417L;
    
    private MonochromeButton startButton;
    private MonochromeIconButton changeColorButton;
    private PlayerPanel playerPanel1;
    private PlayerPanel playerPanel2;
    private SettingsPanel settingsPanel;
    
    private MonochromeButton changeWhitePlayerButton;
    private MonochromeButton changeBlackPlayerButton;
    
    private Player p1;
    private Player p2;
    private JComboBox<GameDifficulty> difficultySelector;

    /**
     * Constructor for the class/view
     */
    public NewSessionView() {
        build();
    }

    /**
     * Method to render the view and insert all necessary elements
     */
    private void build() {
        
        p1 = null;
        p2 = null;
        
        playerPanel1 = new PlayerPanel(p1, SquareColor.WHITE);
        playerPanel1.setBounds(37, 35, 332, 141);
        playerPanel2 = new PlayerPanel(p2, SquareColor.BLACK);
        playerPanel2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        playerPanel2.setBounds(37, 245, 332, 141);
        settingsPanel = new SettingsPanel();
        settingsPanel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        settingsPanel.setBounds(453, 57, 344, 352);
        
        startButton = new MonochromeButton("Start");
        startButton.setBounds(195, 420, 380, 58);
         
        changeColorButton = new MonochromeIconButton(MonochromeIconType.SWITCH, "MonochromeIconButton", "BLACK");
        changeColorButton.setBounds(175, 190, 55, 55);
        changeColorButton.setSizeBig();
        setLayout(null);
        add(changeColorButton);
         
        changeWhitePlayerButton = new MonochromeButton("Change");
        changeWhitePlayerButton.setBounds(250, 75, 105, 50);
        add(changeWhitePlayerButton);
        
        changeBlackPlayerButton = new MonochromeButton("Change");
        changeBlackPlayerButton.setBounds(250, 285, 105, 50);
        add(changeBlackPlayerButton);
         
        add(playerPanel1);
        add(playerPanel2);
        add(settingsPanel);
        add(startButton);
        add(changeColorButton);
        
     // Populate the comboBox_GameLevel with enum values
        difficultySelector = new JComboBox<>(GameDifficulty.values());
        difficultySelector.setBounds(262, 201, 105, 44);
        add(difficultySelector);
        
        JLabel difficultyLabel = new JLabel();
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setFont(new Font("Serif", Font.BOLD, 14));
        difficultyLabel.setText("Select Difficulty:");
        difficultyLabel.setBounds(39, 201, 126, 44);
        add(difficultyLabel);
        
         
        listenerChangeColorButton();
    }

    /**
     * Getter for the button to change the white player 
     * @return Returns the white player change button
     */
    public MonochromeButton getChangeWhitePlayerButton() {
        return changeWhitePlayerButton;
    }

    /**
     * Getter for the button to change the black player
     * @return Returns the black player change button
     */
    public MonochromeButton getChangeBlackPlayerButton() {
        return changeBlackPlayerButton;
    }

    /**
     * Method to swap the colors of the two players when clicking the color change button
     */
    private void listenerChangeColorButton() {
        changeColorButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}        
            @Override
            public void mouseExited(MouseEvent e) {}            
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {
                Player temp1 = p1;
                Player temp2 = p2;
                setPlayer1(temp2);
                setPlayer2(temp1);
            }
        });
    }

    /**
     * Setter for the white player
     * @param whitePlayer The white player passed as a parameter
     */
    public void setPlayer1(Player whitePlayer) {
        playerPanel1.setPlayer(whitePlayer);
        p1 = whitePlayer;
    }

    /**
     * Setter for the black player
     * @param blackPlayer The black player passed as a parameter
     */
    public void setPlayer2(Player blackPlayer) {
        playerPanel2.setPlayer(blackPlayer);
        p2 = blackPlayer;
    }

    /**
     * Getter for the start game button
     * @return Returns the start button
     */
    public MonochromeButton getStartButton() {
        return startButton;
    }

    /**
     * Getter for the button to change the players' colors
     * @return Returns the button to change players' colors
     */
    public MonochromeIconButton getChangeColorButton() {
        return changeColorButton;
    }

    /**
     * Getter for the white player's panel
     * @return Returns the instance of the white player's panel
     */
    public PlayerPanel getPlayerPanel1() {
        return playerPanel1;
    }

    /**
     * Getter for the black player's panel
     * @return Returns the instance of the black player's panel
     */
    public PlayerPanel getPlayerPanel2() {
        return playerPanel2;
    }

    /**
     * Getter for the settings panel
     * @return Returns the instance of the settings panel
     */
    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }
    
    public GameDifficulty getSelectedDifficulty() {
        return (GameDifficulty) difficultySelector.getSelectedItem();
    }

    @Override
    protected void paintComponent(Graphics g) {
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
        g2.drawRect(2, 0, w - 5, h - 5 );
        
        g2.dispose(); 
    }
}

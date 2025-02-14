package com.HyenaBgammon.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuView extends MonochromeView {

    /**
     * This class allows us to visualize the menu of our game.
     */
    private static final long serialVersionUID = 3060121008717453091L;
    public static final String img_menu = "images/menu.png";
    
    // Declaration of button variables
    private MonochromeButton newSessionButton;
    private MonochromeButton loadSessionButton;
    private MonochromeButton playerListButton;
    private MonochromeButton rulesButton;
    private MonochromeButton quitButton;
    private ImageIcon icon;
    private MonochromeButton manageButton;
    private MonochromeButton historyButton;
    
    public MenuView(){
        build();
    }
    
    private void build() {
    	
    	// Title Label
    	JLabel titleLabel = new JLabel("Hyena Backgammon");
    	titleLabel.setFont(new Font("Verdana", Font.BOLD, 30)); // Set custom font
    	titleLabel.setForeground(Color.BLACK);
    	titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align text
    	titleLabel.setBounds(200, 80, 400, 50); // Position at the top-center
    	add(titleLabel);
        
        // Retrieve the image
        try{
            icon = new ImageIcon(img_menu);
        }catch(Exception err){
            System.err.println(err);
        }
        
        // Set layout to allow movement of panels within the main panel
        setLayout(null);
        
        // Declaration of two panels: one for the buttons and another for centering the panel
        JPanel gridContainer = new JPanel();
        JPanel buttonContainer = new JPanel();
        
        GridLayout gl = new GridLayout(7, 1); // To arrange buttons in a column
        
        // Settings for the grid container
        gridContainer.setBounds(200, 150, 400, 400); // Relative positioning of the container with its dimensions
        gridContainer.setOpaque(false);
        
        // Settings for the button container
        gl.setVgap(10); // Space of 10 between each button
        buttonContainer.setLayout(gl);
        buttonContainer.setPreferredSize(new Dimension(400,350)); // Resizing the window
        buttonContainer.setOpaque(false); // Transparency of the window to match background
        
        newSessionButton = new MonochromeButton("New Session");
        buttonContainer.add(newSessionButton);
        
        loadSessionButton = new MonochromeButton("Load Session");
        buttonContainer.add(loadSessionButton);
        
        playerListButton = new MonochromeButton("Player List");
        buttonContainer.add(playerListButton);
        
        historyButton = new MonochromeButton("Game History");
        buttonContainer.add(historyButton);
        
        rulesButton = new MonochromeButton("Rules");
        buttonContainer.add(rulesButton);
        
        manageButton = new MonochromeButton("Questions");
        buttonContainer.add(manageButton);
        
        
        quitButton = new MonochromeButton("Quit");
        quitButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        buttonContainer.add(quitButton);
        
        add(gridContainer);
        gridContainer.add(buttonContainer);
        
    }
    
    /**
     * Getter for the new session button
     * @return Returns the new session button
     */
    public MonochromeButton getNewSessionButton() {
        return newSessionButton;
    }

    /**
     * Getter for the load session button
     * @return Returns the load session button
     */
    public MonochromeButton getLoadSessionButton() {
        return loadSessionButton;
    }

    /**
     * Getter for the player list button
     * @return Returns the player list button
     */
    public MonochromeButton getAddButton() {
        return playerListButton;
    }

    /**
     * Getter for the rules button
     * @return Returns the rules button
     */
    public MonochromeButton getHelpButton() {
        return rulesButton;
    }
    
    /**
     * Getter for the quit button
     * @return Returns the quit button
     */
    public MonochromeButton getQuitButton() {
        return quitButton;
    }
    
    public MonochromeButton getManageButton() {
		return manageButton;
	}
    
    public MonochromeButton getHistoryButton() {
        return historyButton;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon != null) {
            g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    /*
	@Override
    protected void paintComponent(Graphics g) {
        
        g.drawImage(icon.getImage(), 0, 0, this);
        
    }
    */
}

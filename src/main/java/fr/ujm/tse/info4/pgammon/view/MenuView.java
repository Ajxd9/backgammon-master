package fr.ujm.tse.info4.pgammon.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import fr.ujm.tse.info4.pgammon.gui.MonochromeButton;
import fr.ujm.tse.info4.pgammon.gui.MonochromeView;


public class MenuView extends MonochromeView {

    /**
     * This class allows us to visualize the menu of our game.
     */
    private static final long serialVersionUID = 3060121008717453091L;
    public static final String img_menu = "images/menu_bg.png";
    
    // Declaration of button variables
    private MonochromeButton newSessionButton;
    private MonochromeButton loadSessionButton;
    private MonochromeButton playerListButton;
    private MonochromeButton rulesButton;
    private MonochromeButton quitButton;
    
    private ImageIcon icon;
    
    public MenuView(){
        build();
    }
    
    private void build() {
        
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
        
        GridLayout gl = new GridLayout(6, 1); // To arrange buttons in a column
        
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
        
        rulesButton = new MonochromeButton("Rules");
        buttonContainer.add(rulesButton);
        
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);
        buttonContainer.add(emptyPanel);
        
        quitButton = new MonochromeButton("Quit");
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
    public MonochromeButton getPlayerListButton() {
        return playerListButton;
    }

    /**
     * Getter for the rules button
     * @return Returns the rules button
     */
    public MonochromeButton getRulesButton() {
        return rulesButton;
    }
    
    /**
     * Getter for the quit button
     * @return Returns the quit button
     */
    public MonochromeButton getQuitButton() {
        return quitButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        g.drawImage(icon.getImage(), 0, 0, this);
        
    }
}

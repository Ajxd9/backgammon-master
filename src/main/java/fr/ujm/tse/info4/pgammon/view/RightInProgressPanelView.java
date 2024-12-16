package fr.ujm.tse.info4.pgammon.view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ujm.tse.info4.pgammon.gui.ScoreDisplay;
import fr.ujm.tse.info4.pgammon.gui.MonochromeIconType;
import fr.ujm.tse.info4.pgammon.gui.MonochromeIconButton;
import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.Game;

public class RightInProgressPanelView extends JPanel {
    /**
     * This class is used to display the right panel view during a game
     */

    private static final long serialVersionUID = -7846360859879404327L;
    // Components for game actions in the right panel
    private MonochromeIconButton backButton;
    private MonochromeIconButton diceButton;
    private ScoreDisplay doublingCubeDisplay;
    private Game game;
    
    /**
     * Constructor for the panel
     * @param g requires a game as a parameter
     */
    public RightInProgressPanelView(Game g) {
        game = g;
        build();
    }

    private void build() {
        setLayout(null);
        setOpaque(false);
        
        //
        // Right-side panel
        //
        
        // Component and label for cancel move
        backButton = new MonochromeIconButton(MonochromeIconType.BACK, "MonochromeIconButton", "BLACK");
        backButton.setSizeBig();
        backButton.setBounds(10, 34, backButton.getPreferredSize().width, backButton.getPreferredSize().height);
        add(backButton);
        
        JLabel backLabel = new JLabel("<html>Cancel");
        backLabel.setBounds(20, 79, 80, 60);
        add(backLabel);
        
        // Component and label for dice roll
        diceButton = new MonochromeIconButton(MonochromeIconType.DICE, "MonochromeIconButton", "BLACK");
        diceButton.setSizeBig();
        diceButton.setBounds(10, 259, diceButton.getPreferredSize().width, diceButton.getPreferredSize().height);
        add(diceButton);
        
        JLabel diceLabel = new JLabel("<html>Launch<br>Dice");
        diceLabel.setForeground(new Color(0xCCCCCC));
        diceLabel.setBounds(20, 304, 80, 60);
        add(diceLabel);
        
        // Component and label for doubling cube
        doublingCubeDisplay = new ScoreDisplay(game.getDoublingCube().getValue(), SquareColor.WHITE);
        doublingCubeDisplay.setBounds(10, 379, diceButton.getPreferredSize().width, diceButton.getPreferredSize().height);
        add(doublingCubeDisplay);
        
        JLabel doublingCubeLabel = new JLabel("<html>Doubling<br>Cube");
        doublingCubeLabel.setForeground(new Color(0xCCCCCC));
        doublingCubeLabel.setBounds(20, 429, 80, 60);
        add(doublingCubeLabel);
    }

    /**
     * Updates the doubling cube
     */
    public void updateDoublingCube() {
        doublingCubeDisplay.setScore(game.getDoublingCube().getValue());
    }

    /**
     * Getter for the back button
     * @return returns the back button component
     */
    public MonochromeIconButton getBackButton() {
        return backButton;
    }
    
    /**
     * Getter for the dice button
     * @return returns the dice button component
     */
    public MonochromeIconButton getDiceButton() {
        return diceButton;
    }
    
    /**
     * Getter for the doubling cube
     * @return returns the doubling cube component
     */
    public ScoreDisplay getDoublingCubeDisplay() {
        return doublingCubeDisplay;
    }
            
    /**
     * Setter for the game
     * @param game updates the game instance
     */
    public void setGame(Game game) {
        this.game = game;
    }

}

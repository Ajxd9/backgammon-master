package fr.ujm.tse.info4.pgammon.view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ujm.tse.info4.pgammon.gui.ScoreDisplay;
import fr.ujm.tse.info4.pgammon.gui.MonochromeIconType;
import fr.ujm.tse.info4.pgammon.gui.MonochromeIconButton;
import fr.ujm.tse.info4.pgammon.models.SquareColor;

public class BottomInProgressPanelView  extends JPanel {
    /**
     * This class displays the bottom panel during a game
     */
    
    private static final long serialVersionUID = 1587735370301921370L;
    
    // Bottom panel components
    private MonochromeIconButton helpButton;
    private MonochromeIconButton newSessionButton;
    private MonochromeIconButton interruptSessionButton;
    private ScoreDisplay player1ScoreDisplay;
    private ScoreDisplay player2ScoreDisplay;
    
    private int player1Score;
    private int player2Score;

    /**
     * Class constructor
     */
    public BottomInProgressPanelView() {
        build();
    }

    private void build() {
        setLayout(null);
        setOpaque(false);
        
        // Bottom panel
        
        // Help component and label
        helpButton = new MonochromeIconButton(MonochromeIconType.HELP, "MonochromeIconButton", "BLACK");
        helpButton.setSizeBig();
        helpButton.setBounds(600, 5, helpButton.getPreferredSize().width, helpButton.getPreferredSize().height);
        add(helpButton);
        
        JLabel helpLabel = new JLabel("Rules");
        helpLabel.setForeground(new Color(0xCCCCCC));
        helpLabel.setBounds(610, 50, 80, 60);
        add(helpLabel);
        
        // New session component and label
        newSessionButton = new MonochromeIconButton(MonochromeIconType.PLUS, "MonochromeIconButton", "BLACK");
        newSessionButton.setSizeBig();
        newSessionButton.setBounds(700, 5, newSessionButton.getPreferredSize().width, newSessionButton.getPreferredSize().height);
        add(newSessionButton);
        newSessionButton.setVisible(false);
        
        JLabel newSessionLabel = new JLabel("New Session");
        newSessionLabel.setForeground(new Color(0xCCCCCC));
        newSessionLabel.setBounds(680, 50, 130, 60);
        add(newSessionLabel);
        newSessionLabel.setVisible(false);
        
        // Interrupt session component and label
        interruptSessionButton = new MonochromeIconButton(MonochromeIconType.X_BLACK, "MonochromeIconButton", "BLACK");
        interruptSessionButton.setSizeBig();
        interruptSessionButton.setBounds(500, 5, interruptSessionButton.getPreferredSize().width, interruptSessionButton.getPreferredSize().height);
        add(interruptSessionButton);
        
        JLabel interruptSessionLabel = new JLabel("Interrupt session");
        interruptSessionLabel.setForeground(new Color(0xCCCCCC));
        interruptSessionLabel.setBounds(470, 50, 130, 60);
        add(interruptSessionLabel);
        
        // Score component and label
        player1ScoreDisplay = new ScoreDisplay(player1Score, SquareColor.WHITE);
        player1ScoreDisplay.setBounds(270, 5, interruptSessionButton.getPreferredSize().width, interruptSessionButton.getPreferredSize().height);
        add(player1ScoreDisplay);
        
        player2ScoreDisplay = new ScoreDisplay(player2Score, SquareColor.BLACK);
        player2ScoreDisplay.setBounds(350, 5, interruptSessionButton.getPreferredSize().width, interruptSessionButton.getPreferredSize().height);
        add(player2ScoreDisplay);
        
        JLabel scoresLabel = new JLabel("Scores");
        scoresLabel.setForeground(new Color(0xCCCCCC));
        scoresLabel.setBounds(320, 50, 130, 60);
        add(scoresLabel);
    }
    
    /**
     * Updates the scores
     * @param whitePlayerScore integer parameter for the white player's score
     * @param blackPlayerScore integer parameter for the black player's score
     */
    public void updateScore(int whitePlayerScore, int blackPlayerScore) {
        player1Score = whitePlayerScore;
        player2Score = blackPlayerScore;
        player1ScoreDisplay.setScore(player1Score);
        player2ScoreDisplay.setScore(player2Score);
    }

    /**
     * Getter for the help button
     * @return returns the help button class
     */
    public MonochromeIconButton getHelpButton() {
        return helpButton;
    }

    /**
     * Getter for the new session button
     * @return returns the new session button class
     */
    public MonochromeIconButton getNewSessionButton() {
        return newSessionButton;
    }

    /**
     * Getter for the interrupt session button
     * @return returns the interrupt session button class
     */
    public MonochromeIconButton getInterruptSessionButton() {
        return interruptSessionButton;
    }

    /**
     * Getter for the white player's score
     * @return returns the white player's score
     */
    public int getPlayer1Score() {
        return player1Score;
    }

    /**
     * Getter for the black player's score
     * @return returns the black player's score
     */
    public int getPlayer2Score() {
        return player2Score;
    }
}

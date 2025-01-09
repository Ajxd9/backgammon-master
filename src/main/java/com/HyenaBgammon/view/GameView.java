package com.HyenaBgammon.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.swing.*;

import com.HyenaBgammon.models.SquareColor;
import com.HyenaBgammon.models.SessionState;
import com.HyenaBgammon.models.Game;
import com.HyenaBgammon.models.Question;

public class GameView extends MonochromeView {
    
    /**
     * This class manages the entire view of a game.
     * It is composed of several other views, which allows us to manage them.
     */
    private static final long serialVersionUID = 2417367501490643145L;
    
    // Retrieve background image
    public static final ImageIcon backgroundImage = new ImageIcon("images/fond_partie.png");
    public static final ImageIcon bigArrowImage = new ImageIcon("images/fleche_big.png");

    private Game game;
    private BoardView boardView;
    private SessionState state;

    private CompletedGameViewRight rightPanelReview;
    private RightInProgressPanelView rightPanelInProgress;
    private BottomInProgressPanelView inProgressViewBottom;
    private CompletedGameViewBottom finishedViewBottom;
    private PlayerPanelGameView playerPanel1;
    private PlayerPanelGameView playerPanel2;
    private ClockBar clockBar;
    private JLabel diffLabel;

    /**
     * Constructor for the GameView class.
     * This function initializes the GameView page.
     * @param game A Game object
     */
    public GameView(Game game) {
        this.game = game;
        boardView = new BoardView(game);

        setOpaque(false);
        build();
    }

    /**
     * Method to construct the elements of the view and call all the game views.
     */
    private void build() {
        setPreferredSize(new Dimension(1000,800));
        setOpaque(false);

        setLayout(null);
        boardView.setBounds(150, 5, 550, 450);
        add(boardView);

        state = SessionState.IN_PROGRESS;

        clockBar = new ClockBar(null);
        clockBar.setBounds(122,455,598,20);        
        add(clockBar);
        
        // Create game panels
        rightPanelInProgress = new RightInProgressPanelView(game);
        rightPanelInProgress.setBounds(720,0,80,476);
        add(rightPanelInProgress);

        finishedViewBottom = new CompletedGameViewBottom();
        finishedViewBottom.setBounds(0,480,800,95);
        add(finishedViewBottom);
        
        inProgressViewBottom = new BottomInProgressPanelView();
        inProgressViewBottom.setBounds(0,480,800,95);
        add(inProgressViewBottom);
        
        rightPanelReview = new CompletedGameViewRight();
        rightPanelReview.setBounds(720,0,80,476);
        add(rightPanelReview);
        
        playerPanel1 = new PlayerPanelGameView(game.getGameParameters().getWhitePlayer(), SquareColor.WHITE);
        playerPanel1.setBounds(10, 5, 150, 210);
        add(playerPanel1);
        
        playerPanel2 = new PlayerPanelGameView(game.getGameParameters().getBlackPlayer(), SquareColor.BLACK);
        playerPanel2.setBounds(10, 235, 150, 215);
        add(playerPanel2);
        
        diffLabel = new JLabel("Difficulty: " + game.getGameParameters().getDifficulty().name());
        diffLabel.setFont(new Font("Arial", Font.BOLD, 16));
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setBounds(750, 50, 200, 30); // Adjust position as needed
        add(diffLabel);
        revalidate();
        repaint();

        setState(getState());
    }
    public void displayColoredRequestWindow(String title, String message, String color) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        // Create a label for the message with colored text
        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        if ("green".equalsIgnoreCase(color)) {
            messageLabel.setForeground(Color.GREEN);
        } else if ("red".equalsIgnoreCase(color)) {
            messageLabel.setForeground(Color.RED);
        } else {
            messageLabel.setForeground(Color.BLACK); // Default to black if color is unspecified
        }

        dialog.add(messageLabel, BorderLayout.CENTER);

        // Add an "OK" button to close the dialog
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


    /**
     * Displays a question to the player with choices.
     * @param difficulty The difficulty level of the question.
     * @param timeLimit The time limit in seconds for answering.
     * @param callback A callback function to handle the result (true for correct, false for incorrect).
     */
    public void displayQuestion(Question question, int timeLimit, Consumer<Boolean> callback) {
        JDialog questionDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Question", true);
        questionDialog.setLayout(new BorderLayout());
        questionDialog.setSize(500, 400);
        questionDialog.setLocationRelativeTo(this);

        // Display the question text
        JLabel questionLabel = new JLabel("<html><center>" + question.getQuestion() + "</center></html>", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionDialog.add(questionLabel, BorderLayout.NORTH);

        // Create answer buttons dynamically
        JPanel answersPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup answerGroup = new ButtonGroup();

        int index = 0;
        for (String answer : question.getAnswers()) {
            JRadioButton answerButton = new JRadioButton(answer);
            answerButton.setActionCommand(String.valueOf(index + 1)); // Use index as action command
            answerGroup.add(answerButton);
            answersPanel.add(answerButton);
            index++;
        }
        questionDialog.add(answersPanel, BorderLayout.CENTER);

        // Add a submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String selectedAnswer = answerGroup.getSelection() != null ? answerGroup.getSelection().getActionCommand() : null;

            // Validate the selected answer
            boolean isCorrect = question.isCorrect(selectedAnswer);
            callback.accept(isCorrect);
            questionDialog.dispose();
        });
        questionDialog.add(submitButton, BorderLayout.SOUTH);

        // Add a timer for automatic submission
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                callback.accept(false); // Time's up
                questionDialog.dispose();
            }
        };

        timer.schedule(task, timeLimit * 1000);

        // Cancel the timer when the dialog is closed
        questionDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                timer.cancel();
            }
        });

        questionDialog.setVisible(true);
        timer.cancel(); // Cancel the timer if the dialog is closed
    }



    /**
     * Getter for the game
     * @return Returns the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setter for the game
     * This method allows changing the game values
     * @param game The game to set
     */
    public void setGame(Game game) {
        this.game = game;
        boardView.setVisible(false);
        boardView = new BoardView(game);
        boardView.setBounds(173, 30, 547, 446);
        
        rightPanelInProgress.setGame(game);
        add(boardView);
    }

    /**
     * Getter for the session state
     * @return Retrieves the session state
     */
    public SessionState getState() {
        return state;
    }

    /**
     * Setter for the session state
     * This method manages the display of views based on the session state
     * @param state The state enumeration
     */
    public void setState(SessionState state) {
        this.state = state;
        // Change panels based on the state
        if (state.equals(SessionState.IN_PROGRESS)) {
            rightPanelInProgress.setVisible(true);
            rightPanelReview.setVisible(false);
        } else {
            rightPanelInProgress.setVisible(false);
            rightPanelReview.setVisible(true);
        }

        if (state.equals(SessionState.REPLAY)) {
            finishedViewBottom.setVisible(true);
            inProgressViewBottom.setVisible(false);
        } else {
            finishedViewBottom.setVisible(false);
            inProgressViewBottom.setVisible(true);
        }
    }

    /**
     * Getter for the board view
     * @return Returns the board view
     */
    public BoardView getBoardView() {
        return boardView;
    }

    /**
     * Getter for the bottom view in progress state of the game
     * @return Returns the in-progress bottom panel
     */
    public BottomInProgressPanelView getInProgressViewBottomPanel() {
        return inProgressViewBottom;
    }

    /**
     * Getter for the right view in progress state of the game
     * @return Returns the in-progress right panel
     */
    public RightInProgressPanelView getRightPanelInProgress() {
        return rightPanelInProgress;
    }

    /**
     * Getter for the right view in finished state of the game
     * @return Returns the finished right panel
     */
    public CompletedGameViewRight getRightPanelReview() {
        return rightPanelReview;
    }

    /**
     * Setter for the right panel in finished state of the game
     * @param rightPanelReview The right panel to set
     */
    public void setRightPanelReview(CompletedGameViewRight rightPanelReview) {
        this.rightPanelReview = rightPanelReview;
    }

    /**
     * Getter for the finished bottom panel
     * @return Returns the finished bottom panel
     */
    public CompletedGameViewBottom getFinishedViewBottomPanel() {
        return finishedViewBottom;
    }

    /**
     * Getter for the white player's panel
     * @return Returns the white player's panel
     */
    public PlayerPanelGameView getPlayerPanel1() {
        return playerPanel1;
    }

    /**
     * Getter for the black player's panel
     * @return Returns the black player's panel
     */
    public PlayerPanelGameView getPlayerPanel2() {
        return playerPanel2;
    }

    /**
     * Override method to create components
     * This method will display the background image
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(backgroundImage.getImage(), 0, 0, this);
        super.paintComponent(g);
    }

    /**
     * Returns the ClockBar component of the view.
     * @return clockBar : The ClockBar component
     */
    public ClockBar getClockBar() {
        return clockBar;
    }
    public void lockInputs() {
        // Disable all input-related components
        boardView.setEnabled(false); // Disable the board
        clockBar.setEnabled(false);  // Disable the clock bar
        playerPanel1.setEnabled(false); // Disable player 1 panel
        playerPanel2.setEnabled(false); // Disable player 2 panel
    }

    public void unlockInputs() {
        // Re-enable all input-related components
        boardView.setEnabled(true); // Enable the board
        clockBar.setEnabled(true);  // Enable the clock bar
        playerPanel1.setEnabled(true); // Enable player 1 panel
        playerPanel2.setEnabled(true); // Enable player 2 panel
    }

}

package com.HyenaBgammon.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import com.HyenaBgammon.models.AssistantLevel;
import com.HyenaBgammon.models.GameDifficulty;
import com.HyenaBgammon.models.GameParameters;
import com.HyenaBgammon.models.Player;

import javax.swing.ButtonGroup;

public class SettingsPanel extends MonochromePanel {
    private static final long serialVersionUID = -4599011779523529733L;

    private MonochromeIconButton increaseGames;
    private MonochromeIconButton decreaseGames;
    private MonochromeIconButton infiniteGames;
    private MonochromeIconButton increaseTime;
    private MonochromeIconButton decreaseTime;
    private MonochromeIconButton infiniteTime;

    public static final String gamesImage = "images/parties.png";
    public static final String timeImage = "images/time.png";

    private ImageIcon gamesIcon;
    private ImageIcon timeIcon;

    private MonochromeLabel gamesLabel;
    private MonochromeLabel timeLabel;

    private JLabel gamesText;
    private JLabel timeText;

    private MonochromeCheckbox doublingCube;

    private int numberOfGames;
    private int timeLimit;
    
    private String selectedSet;

    // New fields for Player vs AI and Player vs Player selection
    private JRadioButton playerVsPlayerButton;
    private JRadioButton playerVsAIButton;
    private ButtonGroup gameModeGroup;
    private JLabel player2Selection; // Store Player 2 selection
    private GameParameters gameParams;  // Declare it here
    private NewSessionView newSessionView;

    /**
     * Constructor of the class.
     */
    public SettingsPanel() {
        super("Parameters");
        gameParams = new GameParameters(false);  // Default to Player vs Player
        build();  // Now build the UI
    }

    public void build() {
        // Load the images
        try {
            gamesIcon = new ImageIcon(gamesImage);
            timeIcon = new ImageIcon(timeImage);
        } catch (Exception err) {
            System.err.println(err);
        }

        numberOfGames = 1;
        timeLimit = 30;

        gamesText = new JLabel();
        gamesText.setText("Score to achieve");
        gamesText.setForeground(new Color(0xCCCCCC));
        gamesText.setBounds(20, 30, 300, 50);
        add(gamesText);

        timeText = new JLabel();
        timeText.setText("Time limit per turn");
        timeText.setForeground(new Color(0xCCCCCC));
        timeText.setBounds(20, 150, 300, 50);
        add(timeText);

        gamesLabel = new MonochromeLabel(Integer.toString(numberOfGames));
        gamesLabel.setBounds(70, 80, 120, 40);
        add(gamesLabel);

        timeLabel = new MonochromeLabel(Integer.toString(timeLimit) + " s");
        timeLabel.setBounds(70, 200, 120, 40);
        add(timeLabel);

        increaseGames = new MonochromeIconButton(MonochromeIconType.SMALL_ADD, "MonochromeIconButton", "BLACK");
        increaseGames.setSizeSmall();
        increaseGames.setBounds(200, 80, increaseGames.getPreferredSize().width, increaseGames.getPreferredSize().height);
        add(increaseGames);

        decreaseGames = new MonochromeIconButton(MonochromeIconType.SMALL_MINUS, "MonochromeIconButton", "BLACK");
        decreaseGames.setSizeSmall();
        decreaseGames.setBounds(245, 80, decreaseGames.getPreferredSize().width, decreaseGames.getPreferredSize().height);
        add(decreaseGames);

        infiniteGames = new MonochromeIconButton(MonochromeIconType.SMALL_INFINITY, "MonochromeIconButton", "BLACK");
        infiniteGames.setSizeSmall();
        infiniteGames.setBounds(290, 80, infiniteGames.getPreferredSize().width, infiniteGames.getPreferredSize().height);
        add(infiniteGames);

        increaseTime = new MonochromeIconButton(MonochromeIconType.SMALL_ADD, "MonochromeIconButton", "BLACK");
        increaseTime.setSizeSmall();
        increaseTime.setBounds(200, 200, increaseTime.getPreferredSize().width, increaseTime.getPreferredSize().height);
        add(increaseTime);

        decreaseTime = new MonochromeIconButton(MonochromeIconType.SMALL_MINUS, "MonochromeIconButton", "BLACK");
        decreaseTime.setSizeSmall();
        decreaseTime.setBounds(245, 200, decreaseTime.getPreferredSize().width, decreaseTime.getPreferredSize().height);
        add(decreaseTime);

        infiniteTime = new MonochromeIconButton(MonochromeIconType.SMALL_INFINITY, "MonochromeIconButton", "BLACK");
        infiniteTime.setSizeSmall();
        infiniteTime.setBounds(290, 200, infiniteTime.getPreferredSize().width, infiniteTime.getPreferredSize().height);
        add(infiniteTime);

        doublingCube = new MonochromeCheckbox("Doubling Cube");
        doublingCube.setBounds(10, 270, 150, 50);
        doublingCube.setSelected(true);
        add(doublingCube);
        
        JLabel checkerColorLabel = new JLabel("Select Checker Color Set:");
        checkerColorLabel.setForeground(new Color(0xCCCCCC));
        checkerColorLabel.setBounds(220, 270, 150, 50);
        add(checkerColorLabel);

        // Define available color sets
        String[] colorSets = {"Black & White", "Red & Blue"};
        JComboBox<String> colorSetComboBox = new JComboBox<>(colorSets);
        colorSetComboBox.setBounds(220, 320, 150, 50);
        add(colorSetComboBox);

        // Add Action Listener to store selected color set
        String savedSet = gameParams.getCheckerColorSet();
        if (savedSet != null) {
            colorSetComboBox.setSelectedItem(savedSet);
        }
        
        colorSetComboBox.setSelectedItem(gameParams.getCheckerColorSet());
        
        colorSetComboBox.addActionListener(e -> {
            this.selectedSet = (String) colorSetComboBox.getSelectedItem();
            gameParams.setCheckerColorSet(selectedSet); // Store it in GameParameters
        });
        // Add to panel
        add(colorSetComboBox);

        setupIncreaseGamesListener();
        setupDecreaseGamesListener();
        setupInfiniteGamesListener();
        setupIncreaseTimeListener();
        setupDecreaseTimeListener();
        setupInfiniteTimeListener();

        // Add game mode selection
        JLabel modeText = new JLabel("Select Game Mode");
        modeText.setForeground(new Color(0xCCCCCC));
        modeText.setBounds(20, 330, 300, 50);
        add(modeText);

        playerVsPlayerButton = new JRadioButton("Player vs Player");
        playerVsAIButton = new JRadioButton("Player vs AI");
        playerVsAIButton.setSelected(false);
        gameModeGroup = new ButtonGroup();
        gameModeGroup.add(playerVsAIButton);

        gameModeGroup.add(playerVsPlayerButton);
        gameModeGroup.add(playerVsAIButton);

        playerVsPlayerButton.setBounds(50, 380, 150, 30);
        playerVsAIButton.setBounds(50, 420, 150, 30);

        playerVsPlayerButton.setSelected(true); // Default to Player vs Player

        playerVsPlayerButton.setForeground(new Color(0xCCCCCC));
        playerVsAIButton.setForeground(new Color(0xCCCCCC));

        add(playerVsPlayerButton);
        add(playerVsAIButton);

        // Player 2 selection label
        player2Selection = new JLabel("Select Player 2");
        player2Selection.setForeground(new Color(0xCCCCCC));
        player2Selection.setBounds(50, 460, 200, 30);
        add(player2Selection);

        // Add listener to update UI based on selected game mode
        playerVsPlayerButton.addActionListener(e -> {
            System.out.println("Player vs Player button clicked.");
            updatePlayerSelection();
        });

        playerVsAIButton.addActionListener(e -> {
            System.out.println("Player vs AI button clicked.");
            updatePlayerSelection();
        });
}

    /**
     * Checks if Player vs AI mode is selected.
     * @return Returns true if Player vs AI is selected, otherwise false.
     */
    public boolean isPlayerVsAI() {
        if (playerVsAIButton == null) {
            System.out.println("Warning: playerVsAIButton is not initialized yet.");
            return false;  // Default to Player vs Player mode
        }
        return playerVsAIButton.isSelected();
    }
    private void updatePlayerSelection() {
        if (isPlayerVsAI()) {
            // Set Player 2 as AI automatically
            player2Selection.setText("AI Player (Auto)");
            player2Selection.setEnabled(false);
        } else {
            // Allow selecting Player 2 manually
            player2Selection.setText("Select Player 2");
            player2Selection.setEnabled(true);
        }

        // Notify NewSessionView to update Player 2 accordingly
        firePropertyChange("gameModeChanged", null, isPlayerVsAI());
    }





    /**
     * Updates the time value based on the user's choice.
     * @param time Integer value representing the time to display.
     */
    public void updateTimeValue(int time) {
        int minutes = time / 60;
        if (time == 0) {
            timeLabel.setText("\u221E");
        } else if (time > 60) {
            timeLabel.setText(minutes + "m " + (time - 60 * minutes) + "s");
        } else {
            timeLabel.setText(time + " s");
        }
    }

    private void setupIncreaseTimeListener() {
        increaseTime.addMouseListener(new MouseListener() {
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
                timeLimit += 15;
                updateTimeValue(timeLimit);
            }
        });
    }

    private void setupDecreaseTimeListener() {
        decreaseTime.addMouseListener(new MouseListener() {
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
                if (timeLimit > 15) {
                    timeLimit -= 15;
                    updateTimeValue(timeLimit);
                }
            }
        });
    }

    private void setupInfiniteTimeListener() {
        infiniteTime.addMouseListener(new MouseListener() {
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
                timeLimit = 0;
                updateTimeValue(timeLimit);
            }
        });
    }

    /**
     * Updates the number of games based on user choice.
     * @param games Integer representing the number of games.
     */
    public void updateNumberOfGames(int games) {
        if (games == 0) {
            gamesLabel.setText("\u221E");
        } else {
            gamesLabel.setText(Integer.toString(games));
        }
    }

    private void setupIncreaseGamesListener() {
        increaseGames.addMouseListener(new MouseListener() {
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
                numberOfGames++;
                updateNumberOfGames(numberOfGames);
            }
        });
    }

    private void setupDecreaseGamesListener() {
        decreaseGames.addMouseListener(new MouseListener() {
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
                if (numberOfGames > 1) {
                    numberOfGames--;
                    updateNumberOfGames(numberOfGames);
                }
            }
        });
    }

    private void setupInfiniteGamesListener() {
        infiniteGames.addMouseListener(new MouseListener() {
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
                numberOfGames = 0;
                updateNumberOfGames(numberOfGames);
            }
        });
    }

    /**
     * Getter for the number of games.
     * @return Returns the selected number of games.
     */
    public int getNumberOfGames() {
        return numberOfGames;
    }

    /**
     * Getter for the time limit.
     * @return Returns the selected time limit.
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Getter for the games label.
     * @return Returns the label for games.
     */
    public MonochromeLabel getGamesLabel() {
        return gamesLabel;
    }

    /**
     * Getter for the time label.
     * @return Returns the label for time.
     */
    public MonochromeLabel getTimeLabel() {
        return timeLabel;
    }

    /**
     * Getter for the doubling cube checkbox.
     * @return Returns the value of the checkbox.
     */
    public MonochromeCheckbox getdoublingCube() {
        return doublingCube;
    }

    /**
     * Setter for the doubling cube checkbox.
     * @param doublingCubeCheckbox Updates the checkbox value.
     */
    public void setdoublingCube(MonochromeCheckbox doublingCube) {
        this.doublingCube = doublingCube;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(gamesIcon.getImage(), 10, 75, this);
        g.drawImage(timeIcon.getImage(), 10, 194, this);
    }
    
    public String getSelectedSet() {
    	return  this.selectedSet;
    }
    
}

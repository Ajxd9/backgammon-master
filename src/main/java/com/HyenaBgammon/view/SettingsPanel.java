package com.HyenaBgammon.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SettingsPanel extends MonochromePanel {
    /**
     * This class displays the game parameters that can be selected.
     */
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

    /**
     * Constructor of the class.
     */
    public SettingsPanel() {
        super("Parameters");

        build();
    }

    public void build() {
        // Load the images
        try {
            gamesIcon = new ImageIcon(gamesImage);
            timeIcon = new ImageIcon(timeImage);
        } catch (Exception err) {
            System.err.println(err);
        }

        numberOfGames = 3;
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
        doublingCube.setBounds(100, 270, 150, 50);
        doublingCube.setSelected(true);
        add(doublingCube);

        setupIncreaseGamesListener();
        setupDecreaseGamesListener();
        setupInfiniteGamesListener();
        setupIncreaseTimeListener();
        setupDecreaseTimeListener();
        setupInfiniteTimeListener();
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
}


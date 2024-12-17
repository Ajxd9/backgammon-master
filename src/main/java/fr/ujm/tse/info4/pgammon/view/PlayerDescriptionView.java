package fr.ujm.tse.info4.pgammon.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.ujm.tse.info4.pgammon.gui.ImageAvatar;
import fr.ujm.tse.info4.pgammon.gui.MonochromeButton;
import fr.ujm.tse.info4.pgammon.gui.MonochromeCheckbox;
import fr.ujm.tse.info4.pgammon.gui.MonochromePanel;
import fr.ujm.tse.info4.pgammon.models.Player;
import fr.ujm.tse.info4.pgammon.models.AssistantLevel;

public class PlayerDescriptionView extends MonochromePanel {

    /**
     * This class handles the display of a player's description.
     */
    private static final long serialVersionUID = -7183137442304137995L;

    private Player player;

    public static final String WHITE_PIECE = "images/big_pion_blanc.png";
    public static final String BLACK_PIECE = "images/big_pion_noir.png";
    private ImageIcon playerImage;

    private MonochromeCheckbox possibleMovesCheckbox;
    private MonochromeCheckbox suggestMoveCheckbox;

    private JLabel playerNameLabel;
    private JLabel statisticsLabel;

    private MonochromeButton modifyButton;
    private MonochromeButton deleteButton;

    private ImageAvatar playerAvatar;

    /**
     * Constructor for the player description panel.
     * @param p the player passed as a parameter
     */
    public PlayerDescriptionView(Player p) {
        super("Description");
        player = p;

        build();
        updateData();
    }

    /**
     * Setter for the player.
     * @param p updates the player object
     */
    public void setPlayer(Player p) {
        player = p;
        updateData();
    }

    /**
     * Updates the panel with the player's data.
     */
    public void updateData() {
        if (player == null)
            return;

        playerNameLabel.setText(player.getUsername());
        statisticsLabel.setText("<html>" + player.getStats().getGamesPlayed() +
                "<br>" + player.getStats().getWinCount() +
                "<br>" + player.getStats().getLossCount() +
                "<br>" + (int) (player.getStats().getWinPercentage() * 100) +
                "</html>");

        if (player.getAssistantLevel() == AssistantLevel.FULL) {
            possibleMovesCheckbox.setSelected(true);
            suggestMoveCheckbox.setSelected(true);
        } else if (player.getAssistantLevel() == AssistantLevel.BASIC) {
            possibleMovesCheckbox.setSelected(true);
            suggestMoveCheckbox.setSelected(false);
        } else {
            possibleMovesCheckbox.setSelected(false);
            suggestMoveCheckbox.setSelected(false);
        }

        suggestMoveCheckbox.setEnabled(possibleMovesCheckbox.isSelected());
        playerAvatar.setPath(player.getImageSource());
    }

    private void setupPossibleMovesCheckboxListener() {
        possibleMovesCheckbox.addMouseListener(new MouseListener() {

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
                if (player.getAssistantLevel() == AssistantLevel.NOT_USED)
                    player.setAssistantLevel(AssistantLevel.BASIC);
                else
                    player.setAssistantLevel(AssistantLevel.NOT_USED);

                updateData();
            }
        });
    }

    private void setupSuggestMoveCheckboxListener() {
        suggestMoveCheckbox.addMouseListener(new MouseListener() {

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
                if (player.getAssistantLevel() == AssistantLevel.FULL)
                    player.setAssistantLevel(AssistantLevel.BASIC);
                else
                    player.setAssistantLevel(AssistantLevel.FULL);

                updateData();
            }
        });
    }

    public void build() {

        playerAvatar = new ImageAvatar("");
        playerAvatar.setBounds(25, 40, 105, 105);
        add(playerAvatar);

        JLabel textStat = new JLabel();
        possibleMovesCheckbox = new MonochromeCheckbox("<html> Show<br> possible moves");
        suggestMoveCheckbox = new MonochromeCheckbox("<html> Suggest<br> next move");

        suggestMoveCheckbox.setVisible(false);

        JLabel statsLabel = new JLabel("Statistics");
        statsLabel.setForeground(new Color(0xCCCCCC));
        statsLabel.setBounds(15, 140, 200, 50);
        add(statsLabel);

        JLabel configLabel = new JLabel("Configuration Wizard");
        configLabel.setForeground(new Color(0xCCCCCC));
        configLabel.setBounds(15, 290, 200, 50);
        add(configLabel);

        JPanel playerNamePanel = new JPanel();
        playerNamePanel.setLayout(new BorderLayout());
        playerNamePanel.setBounds(140, 40, 180, 50);
        playerNamePanel.setOpaque(false);

        playerNameLabel = new JLabel();
        playerNameLabel.setForeground(new Color(0xCCCCCC));
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerNameLabel.setHorizontalAlignment(JLabel.CENTER);
        add(playerNamePanel);
        playerNamePanel.add(playerNameLabel);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BorderLayout());
        statsPanel.setBounds(180, 180, 140, 100);
        statsPanel.setOpaque(false);

        statisticsLabel = new JLabel();
        statisticsLabel.setText("test");
        statisticsLabel.setForeground(new Color(0xCCCCCC));
        statisticsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(statsPanel);
        statsPanel.add(statisticsLabel);

        textStat.setForeground(new Color(0xCCCCCC));
        textStat.setText("<html>Games played:<br>Wins:<br>Losses:<br>Win percentage:</html>");
        textStat.setBounds(15, 130, 140, 200);
        textStat.setFont(new Font("Arial", Font.PLAIN, 12));

        suggestMoveCheckbox.setForeground(new Color(0xCCCCCC));
        suggestMoveCheckbox.setBounds(180, 320, 200, 50);
        suggestMoveCheckbox.setOpaque(false);

        possibleMovesCheckbox.setForeground(new Color(0xCCCCCC));
        possibleMovesCheckbox.setBounds(15, 320, 200, 50);
        possibleMovesCheckbox.setOpaque(false);

        add(possibleMovesCheckbox);
        add(suggestMoveCheckbox);
        add(textStat);

        modifyButton = new MonochromeButton("Modify");
        modifyButton.setBounds(15, 380, 140, 50);
        add(modifyButton);

        deleteButton = new MonochromeButton("Delete");
        deleteButton.setBounds(175, 380, 140, 50);
        add(deleteButton);

        setupPossibleMovesCheckboxListener();
        setupSuggestMoveCheckboxListener();
    }

    /**
     * Getter for the modify button.
     * @return the modify button component
     */
    public MonochromeButton getModifyButton() {
        return modifyButton;
    }

    /**
     * Getter for the delete button.
     * @return the delete button component
     */
    public MonochromeButton getDeleteButton() {
        return deleteButton;
    }

    /**
     * Getter for the player's image.
     * @return the player's image component
     */
    public ImageIcon getPlayerImage() {
        return playerImage;
    }

    /**
     * Setter for the player's image.
     * @param playerImage updates the player's avatar image
     */
    public void setPlayerImage(ImageIcon playerImage) {
        this.playerImage = playerImage;
    }

    /**
     * Getter for the possible moves checkbox.
     * @return the possible moves checkbox component
     */
    public MonochromeCheckbox getPossibleMovesCheckbox() {
        return possibleMovesCheckbox;
    }

    /**
     * Setter for the possible moves checkbox.
     * @param possibleMovesCheckbox updates the checkbox
     */
    public void setPossibleMovesCheckbox(MonochromeCheckbox possibleMovesCheckbox) {
        this.possibleMovesCheckbox = possibleMovesCheckbox;
    }

    /**
     * Getter for the suggest move checkbox.
     * @return the suggest move checkbox component
     */
    public MonochromeCheckbox getSuggestMoveCheckbox() {
        return suggestMoveCheckbox;
    }

    /**
     * Setter for the suggest move checkbox.
     * @param suggestMoveCheckbox updates the checkbox
     */
    public void setSuggestMoveCheckbox(MonochromeCheckbox suggestMoveCheckbox) {
        this.suggestMoveCheckbox = suggestMoveCheckbox;
    }

    /**
     * Getter for the player.
     * @return the current player
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}

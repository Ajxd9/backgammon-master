package fr.ujm.tse.info4.pgammon.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.Player;
import fr.ujm.tse.info4.pgammon.models.AssistantLevel;

public class PlayerPanel extends MonochromePanel {

    /**
     * This class allows modifying the player panel in the new session view.
     */
    private static final long serialVersionUID = 7553310687895062778L;
    private Player player;
    private SquareColor color;

    public static final String whiteChecker = "images/big_pion_blanc.png";
    public static final String blackChecker = "images/big_pion_noir.png";
    private ImageIcon checkerImage;

    private MonochromeCheckbox showPossibleMovesCheckbox;
    private MonochromeCheckbox suggestNextMoveCheckbox;
    private JLabel playerStatsLabel;
    private ImageAvatar playerImage;

    /**
     * Constructor for the player panel.
     * @param p Player passed as a parameter.
     * @param c CouleurCase passed as a parameter.
     */
    public PlayerPanel(Player p, SquareColor c) {
        super("");
        player = p;
        color = c;

        build();
        updateData();
    }

    /**
     * Setter for the player.
     * @param p Changes the player value.
     */
    public void setPlayer(Player p) {
        player = p;
        updateData();
    }

    /**
     * Updates the panel data.
     */
    public void updateData() {
        if (player != null) {
            setTitle(player.getUsername());
            playerStatsLabel.setText("<html> Wins: "
                    + player.getStats().getWinCount()
                    + "<br>Losses: " + player.getStats().getLossCount());

            playerImage.setPath(player.getImageSource());

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

            suggestNextMoveCheckbox.setEnabled(showPossibleMovesCheckbox.isSelected());

            playerImage.setVisible(true);
            showPossibleMovesCheckbox.setVisible(true);
        } else {
            setTitle("");
            playerStatsLabel.setText("");

            playerImage.setVisible(false);
            showPossibleMovesCheckbox.setVisible(false);
            suggestNextMoveCheckbox.setVisible(false);
        }
    }

    private void setupShowPossibleMovesListener() {
        showPossibleMovesCheckbox.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (player.getAssistantLevel() == AssistantLevel.NOT_USED) {
                    player.setAssistantLevel(AssistantLevel.BASIC);
                } else {
                    player.setAssistantLevel(AssistantLevel.NOT_USED);
                }
                updateData();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {}
        });
    }

    private void setupSuggestNextMoveListener() {
        suggestNextMoveCheckbox.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (player.getAssistantLevel() == AssistantLevel.FULL) {
                    player.setAssistantLevel(AssistantLevel.BASIC);
                } else {
                    player.setAssistantLevel(AssistantLevel.FULL);
                }
                updateData();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {}
        });
    }

    public void build() {
        // Load the checker image
        try {
            if (color.equals(SquareColor.WHITE)) {
                checkerImage = new ImageIcon(whiteChecker);
            } else {
                checkerImage = new ImageIcon(blackChecker);
            }
        } catch (Exception err) {
            System.err.println(err);
        }

        playerImage = new ImageAvatar("");
        playerImage.setBounds(15, 40, 50, 50);
        add(playerImage);

        playerStatsLabel = new JLabel();
        showPossibleMovesCheckbox = new MonochromeCheckbox("<html> Show <br> possible moves");
        suggestNextMoveCheckbox = new MonochromeCheckbox("<html> Suggest <br> next move");

        playerStatsLabel.setForeground(new Color(0xCCCCCC));
        playerStatsLabel.setBounds(130, 40, 200, 50);
        playerStatsLabel.setFont(new Font("Arial", Font.HANGING_BASELINE, 12));

        suggestNextMoveCheckbox.setForeground(new Color(0xCCCCCC));
        suggestNextMoveCheckbox.setBounds(190, 90, 150, 50);
        suggestNextMoveCheckbox.setOpaque(false);

        showPossibleMovesCheckbox.setForeground(new Color(0xCCCCCC));
        showPossibleMovesCheckbox.setBounds(10, 90, 150, 50);
        showPossibleMovesCheckbox.setOpaque(false);

        add(showPossibleMovesCheckbox);
        add(suggestNextMoveCheckbox);
        add(playerStatsLabel);

        setupShowPossibleMovesListener();
        setupSuggestNextMoveListener();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(checkerImage.getImage(), 70, 40, this);
    }
}

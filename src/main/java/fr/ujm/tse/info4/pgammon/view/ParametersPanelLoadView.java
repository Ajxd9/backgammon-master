package fr.ujm.tse.info4.pgammon.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.ujm.tse.info4.pgammon.gui.ScoreDisplay;
import fr.ujm.tse.info4.pgammon.gui.ImageAvatar;
import fr.ujm.tse.info4.pgammon.gui.MonochromeButton;
import fr.ujm.tse.info4.pgammon.gui.MonochromeLabel;
import fr.ujm.tse.info4.pgammon.gui.MonochromePanel;
import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.Session;

public class ParametersPanelLoadView extends MonochromePanel {
    /**
     * This class displays the session parameters panel in the load view.
     */

    private static final long serialVersionUID = 7111414911070584708L;

    private ScoreDisplay scorePlayer1;
    private ScoreDisplay scorePlayer2;

    private MonochromeLabel gamesLabel;
    private MonochromeLabel timeLabel;

    private MonochromeButton deleteButton;

    private JLabel gamesText;
    private JLabel timeText;
    private JLabel doublingCubeText;

    public static final String gamesImage = "images/parties.png";
    public static final String timeImage = "images/time.png";

    private ImageIcon gamesIcon;
    private ImageIcon timeIcon;

    private Session session;

    private ImageAvatar whitePlayerAvatar;
    private ImageAvatar blackPlayerAvatar;

    private JLabel whitePlayerName;
    private JLabel blackPlayerName;

    public static final String whiteChecker = "images/big_pion_blanc.png";
    public static final String blackChecker = "images/big_pion_noir.png";
    private ImageIcon whiteCheckerIcon;
    private ImageIcon blackCheckerIcon;

    /**
     * Constructor for the class.
     * @param s a session passed as a parameter.
     */
    public ParametersPanelLoadView(Session s) {
        super("Parameters");
        session = s;

        build();
    }

    /**
     * Setter for the session.
     * @param s allows changing the current session.
     */
    public void setSession(Session s) {
        session = s;
        updateData();
    }

    /**
     * Updates all session parameters.
     */
    public void updateData() {

        scorePlayer1.setScore(session.getScores().get(session.getCurrentGame().getGameParameters().getWhitePlayer()));
        scorePlayer2.setScore(session.getScores().get(session.getCurrentGame().getGameParameters().getBlackPlayer()));
        whitePlayerAvatar.setPath(session.getCurrentGame().getGameParameters().getWhitePlayer().getImageSource());
        blackPlayerAvatar.setPath(session.getCurrentGame().getGameParameters().getBlackPlayer().getImageSource());
        whitePlayerName.setText(session.getCurrentGame().getGameParameters().getWhitePlayer().getUsername());
        blackPlayerName.setText(session.getCurrentGame().getGameParameters().getBlackPlayer().getUsername());

        int winningGames = session.getGameParameters().getWinningGamesCount();

        if (winningGames == 0) {
            gamesLabel.setText("\u221E");
        } else {
            gamesLabel.setText(Integer.toString(winningGames));
        }

        int timeInSeconds = session.getGameParameters().getSecondsPerTurn() / 1000;
        int minutes = timeInSeconds / 60;
        if (timeInSeconds == 0) {
            timeLabel.setText("\u221E");
        } else if (timeInSeconds > 60) {
            timeLabel.setText(minutes + "m " + (timeInSeconds - 60 * minutes) + "s");
        } else {
            timeLabel.setText(timeInSeconds + " s");
        }

        if (session.getCurrentGame().getGameParameters().isUseDoubling()) {
            doublingCubeText.setText("Doubling cube is used");
        } else {
            doublingCubeText.setText("Doubling cube is not used");
        }
    }

    public void build() {

        setLayout(null);

        // Retrieve images
        gamesIcon = new ImageIcon(gamesImage);
        timeIcon = new ImageIcon(timeImage);
        whiteCheckerIcon = new ImageIcon(whiteChecker);
        blackCheckerIcon = new ImageIcon(blackChecker);

        gamesLabel = new MonochromeLabel("");
        gamesLabel.setBounds(15, 290, 120, 40);
        add(gamesLabel);

        timeLabel = new MonochromeLabel("");
        timeLabel.setBounds(165, 290, 120, 40);
        add(timeLabel);

        scorePlayer1 = new ScoreDisplay(0, SquareColor.WHITE);
        scorePlayer1.setBounds(95, 70, 40, 40);
        add(scorePlayer1);

        scorePlayer2 = new ScoreDisplay(0, SquareColor.BLACK);
        scorePlayer2.setBounds(160, 70, 40, 40);
        add(scorePlayer2);

        whitePlayerAvatar = new ImageAvatar("");
        whitePlayerAvatar.setBounds(15, 70, 70, 70);
        add(whitePlayerAvatar);

        blackPlayerAvatar = new ImageAvatar("");
        blackPlayerAvatar.setBounds(210, 70, 70, 70);
        add(blackPlayerAvatar);

        gamesText = new JLabel();
        gamesText.setText("Score to achieve");
        gamesText.setForeground(new Color(0xCCCCCC));
        gamesText.setBounds(15, 180, 300, 50);
        add(gamesText);

        timeText = new JLabel();
        timeText.setText("<html>Time limit <br>per turn");
        timeText.setForeground(new Color(0xCCCCCC));
        timeText.setBounds(165, 180, 300, 50);
        add(timeText);

        whitePlayerName = new JLabel();
        whitePlayerName.setText("");
        whitePlayerName.setForeground(new Color(0xCCCCCC));
        whitePlayerName.setFont(new Font("Arial", Font.BOLD, 22));
        whitePlayerName.setBounds(15, 28, 120, 50);
        add(whitePlayerName);

        blackPlayerName = new JLabel();
        blackPlayerName.setText("");
        blackPlayerName.setForeground(new Color(0xCCCCCC));
        blackPlayerName.setFont(new Font("Arial", Font.BOLD, 22));
        blackPlayerName.setBounds(160, 28, 120, 50);
        add(blackPlayerName);

        doublingCubeText = new JLabel();
        doublingCubeText.setForeground(new Color(0xCCCCCC));
        doublingCubeText.setBounds(20, 330, 300, 50);
        add(doublingCubeText);

        deleteButton = new MonochromeButton("Delete");
        deleteButton.setForeground(new Color(0xCCCCCC));
        deleteButton.setBounds(165, 340, 120, 40);
        add(deleteButton);
    }

    /**
     * Getter for the delete button.
     * @return returns the delete button class.
     */
    public MonochromeButton getDeleteButton() {
        return deleteButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(gamesIcon.getImage(), 50, 230, this);
        g.drawImage(timeIcon.getImage(), 200, 230, this);
        g.drawImage(whiteCheckerIcon.getImage(), 60, 115, this);
        g.drawImage(blackCheckerIcon.getImage(), 187, 115, this);
    }
}

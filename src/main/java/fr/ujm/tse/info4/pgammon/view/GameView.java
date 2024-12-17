package fr.ujm.tse.info4.pgammon.view;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import fr.ujm.tse.info4.pgammon.gui.ClockBar;
import fr.ujm.tse.info4.pgammon.gui.MonochromeView;
import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.SessionState;
import fr.ujm.tse.info4.pgammon.models.Game;

public class GameView extends MonochromeView {
	
	/**
	 * This class manages the entire view of a game.
	 * It is composed of several other views, which allows us to manage them.
	 */
	private static final long serialVersionUID = 2417367501490643145L;
	
	// Retrieve background image
	public static final ImageIcon backgroundImage = new ImageIcon("images/game_background.png");
	public static final ImageIcon bigArrowImage = new ImageIcon("images/big_arrows.png");

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
		setPreferredSize(new Dimension(800,600));
		setOpaque(false);

		setLayout(null);
		boardView.setBounds(154, -2, 547, 446);
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

		setState(getState());
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
}

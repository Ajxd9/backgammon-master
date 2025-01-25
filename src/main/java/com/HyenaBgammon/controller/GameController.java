// 
//
//  @ Project : Project Gammon
//  @ File : GameController.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package com.HyenaBgammon.controller;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.jdom2.JDOMException;
import com.HyenaBgammon.controller.*;
import com.HyenaBgammon.models.Square;
import com.HyenaBgammon.models.SquareColor;
import com.HyenaBgammon.models.Movement;
import com.HyenaBgammon.models.SessionState;
import com.HyenaBgammon.models.SessionManager;
import com.HyenaBgammon.models.AssistantLevel;
import com.HyenaBgammon.models.Game;
import com.HyenaBgammon.models.GameDifficulty;
import com.HyenaBgammon.models.History;
import com.HyenaBgammon.models.Profiles;
import com.HyenaBgammon.models.Session;
import com.HyenaBgammon.view.GameView;
import com.HyenaBgammon.view.RulesDialog;
import com.HyenaBgammon.view.RulesView;

public class GameController implements Controller
{
	
    private Session session;
    private GameView gameView;
    private BoardController boardController;
    private GameController gameController;
    private JFrame frame;
    private Controller controller;
    private int gameReviewPosition;
    private boolean isForwardDirection;
    private Timer gameReviewTimer;
    private SquareColor doubleStakeColor;
    private HashMap<Integer, History> gameHistory; // To store the history of all games
    private int gameCounter; // Counter to track the number of games played

   

    @Deprecated
    public GameController(Game game)
    {
        gameController = this;
        gameView = new GameView(game);

        build();
        boardController = new BoardController(game, gameView, this);
    }

    public GameController(Session session, Controller controller)
    {
        doubleStakeColor = SquareColor.EMPTY;
        this.controller = controller;
        gameController = this;
        this.session = session;
        gameView = new GameView(session.getCurrentGame());
        gameReviewTimer = null;
        this.gameHistory = new HashMap<>();
        this.gameCounter = 0;
        build();
     
        boardController = new BoardController(session.getCurrentGame(), gameView, this);
        gameView.getInProgressViewBottomPanel().updateScore(session.getScores().get(session.getGameParameters().getWhitePlayer()),
                                                      session.getScores().get(session.getGameParameters().getBlackPlayer()));
    }

    private void build() {
        listenerBack();
        listenerRollDice();
        listenerGetPossibleMovesPlayer1();
        listenerGetPossibleMovesPlayer2();
        listenerDoubleStakeButton();
        listenerNextGame();
        listenerInterruptSession();
        listenerReviewGame();
        listenerGameReviewButtons();
        listenerTimer();
        listenerInterruptGame();
        listenerHelpButton();
    }

    public void listenerTimer()
    {
        gameReviewTimer = new Timer(1000, null);
        gameReviewTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameReviewPosition < session.getCurrentGame().getNumberOfStoredMoves())
                {
                    if(!isForwardDirection)
                    {
                        isForwardDirection = true;
                    }
                    else
                    {
                        gameReviewPosition++;
                    }

                Movement move = session.getCurrentGame().getNextMovement(gameReviewPosition);
                if (move != null)
                    gameView.getFinishedViewBottomPanel().getReplayBar().goTo(move, isForwardDirection);
                }
                else
                {
                    gameReviewTimer.stop();
                }
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });
    }

    private void listenerHelpButton() {
        gameView.getInProgressViewBottomPanel().getHelpButton().addMouseListener(new MouseListener() {
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
            	RulesDialog rulesDialog = new RulesDialog(frame);
                rulesDialog.setVisible(true);
            }
        });
    }

    public void listenerGameReviewButtons()
    {
        gameView.getFinishedViewBottomPanel().getReplayBar().getEndButton().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                if(gameReviewTimer != null)
                    gameReviewTimer.stop();
                isForwardDirection = true;

                for(int i = gameReviewPosition; i < session.getCurrentGame().getNumberOfStoredMoves(); i++)
                {
                    gameReviewPosition++;
                    Movement move = session.getCurrentGame().getNextMovement(gameReviewPosition);
                    if (move != null)
                        gameView.getFinishedViewBottomPanel().getReplayBar().goTo(move, isForwardDirection);
                }

                gameView.getFinishedViewBottomPanel().getReplayBar().goToEnd();
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });

        gameView.getFinishedViewBottomPanel().getReplayBar().getStartButton().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                if(gameReviewTimer != null)
                    gameReviewTimer.stop();
                gameReviewPosition = 0;
                isForwardDirection = true;

                gameView.getFinishedViewBottomPanel().getReplayBar().goToStart();
                session.getCurrentGame().getBoard().resetSquares();
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });

        gameView.getFinishedViewBottomPanel().getReplayBar().getNextButton().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                if(gameReviewTimer != null)
                    gameReviewTimer.stop();
                if (gameReviewPosition < session.getCurrentGame().getNumberOfStoredMoves())
                {
                    if(!isForwardDirection)
                    {
                        isForwardDirection = true;
                    }
                    else
                    {
                        gameReviewPosition++;
                    }

                Movement move = session.getCurrentGame().getNextMovement(gameReviewPosition);
                if (move != null)
                    gameView.getFinishedViewBottomPanel().getReplayBar().goTo(move, isForwardDirection);
                }
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });

        gameView.getFinishedViewBottomPanel().getReplayBar().getPreviousButton().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                if(gameReviewTimer != null)
                    gameReviewTimer.stop();
                if (gameReviewPosition > 0)
                {
                    if(isForwardDirection)
                    {
                        isForwardDirection = false;
                    }
                    else
                    {
                        gameReviewPosition--;
                    }
                    Movement move = session.getCurrentGame().getPreviousMovement(gameReviewPosition);

                    if (move != null)
                        gameView.getFinishedViewBottomPanel().getReplayBar().goTo(move, isForwardDirection);
                }
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });
    }

    public void listenerReviewGame()
    {
        gameView.getRightPanelReview().getUndo().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mousePressed(MouseEvent arg0) {}
            @Override
            public void mouseReleased(MouseEvent arg0) {
                if (gameView.getState() != SessionState.REPLAY)
                {
                    gameReviewPosition = 0;
                    isForwardDirection = true;
                    gameView.setState(SessionState.REPLAY);
                    gameView.getFinishedViewBottomPanel().getReplayBar().setTurns(session.getCurrentGame().getPlayerTurnHistory());
                    session.getCurrentGame().getBoard().resetSquares();
                    gameView.updateUI();
                    gameView.getBoardView().updateUI();
                    gameView.getBoardView().updateDice();
                }
                else
                {
                    if (gameReviewTimer != null)
                        gameReviewTimer.start();
                }
            }
        });
    }

    public void listenerBack()
    {
        gameView.getRightPanelInProgress().getBackButton().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mousePressed(MouseEvent arg0) {}
            @Override
            public void mouseReleased(MouseEvent arg0) {
                session.getCurrentGame().undoLastMove();
                gameView.updateUI();
                gameView.getBoardView().updateDice();
            }
        });
    }

    public void listenerRollDice() {
        gameView.getRightPanelInProgress().getDices().addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Game currentGame = session.getCurrentGame();
                if (currentGame.isTurnFinished() && !currentGame.isGameFinished()) {
                    currentGame.rollDice(); // Delegate dice rolling to Game
                    if (boardController.getClock() != null) {
                        boardController.getClock().restart();
                    }
                    if (!currentGame.hasPossibleMove()) {
                        gameView.displayRequestWindow("No possible move", "");
                        boardController.changeTurn();
                    }
                }
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }

            // Empty implementations for other mouse events
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
    }


    public void listenerGetPossibleMovesPlayer1()
    {
        gameView.getPlayerPanel1().getShowPossibleMoves().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mousePressed(MouseEvent arg0) {}
            @Override
            public void mouseReleased(MouseEvent arg0) {
                if(session.getCurrentGame().getGameParameters().getWhitePlayer().getAssistantLevel() == AssistantLevel.NOT_USED)
                {
                    session.getCurrentGame().getGameParameters().getWhitePlayer().setAssistantLevel(AssistantLevel.BASIC);
                }
                else
                {
                    session.getCurrentGame().getGameParameters().getWhitePlayer().setAssistantLevel(AssistantLevel.NOT_USED);
                    gameView.getBoardView().setPossible(null);
                    gameView.getBoardView().updateUI();
                }
                gameView.getPlayerPanel1().updateData();
            }
        });
    }

    public void listenerGetPossibleMovesPlayer2()
    {
        gameView.getPlayerPanel2().getShowPossibleMoves().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mousePressed(MouseEvent arg0) {}
            @Override
            public void mouseReleased(MouseEvent arg0) {
                if(session.getCurrentGame().getGameParameters().getBlackPlayer().getAssistantLevel() == AssistantLevel.NOT_USED)
                {
                    session.getCurrentGame().getGameParameters().getBlackPlayer().setAssistantLevel(AssistantLevel.BASIC);
                }
                else
                {
                    session.getCurrentGame().getGameParameters().getBlackPlayer().setAssistantLevel(AssistantLevel.NOT_USED);
                    gameView.getBoardView().setPossible(new ArrayList<Square>());
                    gameView.getBoardView().updateUI();
                }
                gameView.getPlayerPanel2().updateData();
            }
        });
    }

    public void listenerDoubleStakeButton()
    {
        if (!session.getGameParameters().isUseDoubling())
        {
            gameView.getRightPanelInProgress().getDoublingCubeDisplay().setEnabled(false);
        }
        else
        {
            gameView.getRightPanelInProgress().getDoublingCubeDisplay().addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent arg0) {}
                @Override
                public void mouseEntered(MouseEvent arg0) {}
                @Override
                public void mouseExited(MouseEvent arg0) {}
                @Override
                public void mousePressed(MouseEvent arg0) {}
                @Override
                public void mouseReleased(MouseEvent arg0) {
                    if ((doubleStakeColor == SquareColor.EMPTY || session.getCurrentGame().getCurrentPlayer() != doubleStakeColor)
                            && (session.getCurrentGame().isTurnFinished() && !session.getCurrentGame().isGameFinished()))
                    {
                        SortedSet<String> options = new ConcurrentSkipListSet<>();
                        options.add("No");
                        options.add("Yes");
                        gameView.displayRequestWindow("Do you accept the doubling cube ?", options).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String action = e.getActionCommand();
                                if (action == "Yes")
                                {
                                    doubleStakeColor = session.getCurrentGame().getCurrentPlayer();
                                    session.getCurrentGame().doubleDoublingCube();
                                }
                                else if (action == "No")
                                {
                                    endGame();
                                }
                                gameView.getRightPanelInProgress().updateDoublingCube();
                            }
                        });
                    }
                }
            });
        }
    }

    public void listenerInterruptGame()
    {
        gameView.getRightPanelReview().getX_white().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mousePressed(MouseEvent arg0) {}
            @Override
            public void mouseReleased(MouseEvent arg0) {
                SortedSet<String> options = new ConcurrentSkipListSet<>();
                options.add("Finish");
                options.add("Cancel");
                options.add("Save");
                gameView.displayRequestWindow("What do you want to do ?", options).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String action = e.getActionCommand();
                        if (action == "Finish")
                        {
                            if (session.getBestPlayer() != null)
                            {
                                session.endSession(session.getBestPlayer());
                            }
                            ((MainController)controller).endSession();
                            controller.back();
                        }
                        else if (action == "Cancel")
                        {
                            // Do nothing
                        }
                        else if (action == "Save")
                        {
                            try {
                                SessionManager management = SessionManager.getSessionManager();
                                management.save();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (JDOMException e1) {
                                e1.printStackTrace();
                            }

                            Profiles profiles = Profiles.getProfiles();
                            profiles.save();
                            controller.back();
                        }
                    }
                });
            }
        });
    }

    public void listenerInterruptSession()
    {
        gameView.getInProgressViewBottomPanel().getInterruptSessionButton().addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mousePressed(MouseEvent arg0) {}
            @Override
            public void mouseReleased(MouseEvent arg0) {
                SortedSet<String> options = new ConcurrentSkipListSet<>();
                options.add("Finish");
                options.add("Cancel");
                options.add("Save");
                gameView.displayRequestWindow("What do you want to do ?", options).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String action = e.getActionCommand();
                        if (action == "Finish")
                        {
                            if (session.getBestPlayer() != null)
                            {
                                session.endSession(session.getBestPlayer());
                            }
                            ((MainController)controller).endSession();
                            controller.back();
                        }
                        else if (action == "Cancel")
                        {
                            // Do nothing
                        }
                        else if (action == "Save")
                        {
                            try {
                                SessionManager management = SessionManager.getSessionManager();
                                management.save();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (JDOMException e1) {
                                e1.printStackTrace();
                            }

                            Profiles profiles = Profiles.getProfiles();
                            profiles.save();
                            controller.back();
                        }
                    }
                });
            }
        });
    }

    public void listenerNextGame() {
        gameView.getRightPanelReview().getNext().addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (session.isSessionFinished()) {
                    controller.back();
                    ((MainController) controller).endSession();
                    Profiles profiles = Profiles.getProfiles();
                    profiles.save();
                } else {
                    newGame(); // Start a new game using the template method
                }
            }

            // Empty implementations for other mouse events
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
    }

    public void newGame() {
        // Reset session and create a new game
        session.newGame(); 
        Game currentGame = session.getCurrentGame();

        // Update the game view and board controller
        gameView.setGame(currentGame); 
        boardController = new BoardController(currentGame, gameView, this);

        // Start the game lifecycle using the template method
        System.out.println("Starting a new game...");
        currentGame.playGame(); 
    }



 
  /*  
    
    public void endGame()
    {
        if (boardController.getClock() != null)
        {
            boardController.getClock().stop();
            boardController.getClock().setValue(0);
        }

        session.endGame();

        gameView.getInProgressViewBottomPanel().updateScore(
                session.getScores().get(session.getGameParameters().getWhitePlayer()),
                session.getScores().get(session.getGameParameters().getBlackPlayer())
        );

        if(session.checkSessionEnd())
        {
            session.endSession();
            gameView.getRightPanelReview().getNextLabel().setText("<html>Finish<br>Session</html>");
            gameView.displayRequestWindow(
                    session.getCurrentGame().getGameParameters().getPlayer(session.getCurrentGame().getCurrentPlayer()).getUsername(),
                    " wins the session!"
            );
        }
        else
        {
            gameView.displayRequestWindow(session.getCurrentGame().getGameParameters().getPlayer(session.getCurrentGame().getCurrentPlayer()).getUsername(),
                    " wins the game!"
            );
        }
        gameView.setState(SessionState.FINISHED);
        
    }
    
    
    */
    
  
    
    public void endGame() {
        // Stop the clock if it exists
        if (boardController.getClock() != null) {
            boardController.getClock().stop();
            boardController.getClock().setValue(0);
        }

        // Get game details
        Game currentGame = session.getCurrentGame();
        String winnerName = currentGame.getGameParameters().getPlayer(currentGame.getCurrentPlayer()).getUsername();
        String whitePlayer = currentGame.getGameParameters().getWhitePlayer().getUsername();
        String blackPlayer = currentGame.getGameParameters().getBlackPlayer().getUsername();

        // Calculate game duration using session
        session.endGame(); // This will set the gameEndTime in the session
        Duration gameDuration = session.getGameDuration(); // Calculate the duration

        GameDifficulty difficultyLevel = currentGame.getGameParameters().getDifficulty();

        // Save the game history
        gameCounter++;
        History historyEntry = new History(
            winnerName,
            whitePlayer + " vs " + blackPlayer,
            gameDuration,
            difficultyLevel,
            session.endGame() // Use the session's gameEndTime
        );
        gameHistory.put(gameCounter, historyEntry);

        // Update UI for scores
        gameView.getInProgressViewBottomPanel().updateScore(
            session.getScores().get(session.getGameParameters().getWhitePlayer()),
            session.getScores().get(session.getGameParameters().getBlackPlayer())
        );

        // Handle session end or game end
        if (session.checkSessionEnd()) {
            session.endSession();
            gameView.getRightPanelReview().getNextLabel().setText("<html>Finish<br>Session</html>");
            gameView.displayRequestWindow(
                session.getCurrentGame().getGameParameters().getPlayer(session.getCurrentGame().getCurrentPlayer()).getUsername(),
                " wins the session!"
            );
        } else {
            gameView.displayRequestWindow(
                session.getCurrentGame().getGameParameters().getPlayer(session.getCurrentGame().getCurrentPlayer()).getUsername(),
                " wins the game!"
            );
        }

        gameView.setState(SessionState.FINISHED);
    }


    public Game getGame() {
        return session.getCurrentGame();
    }

    public GameView getGameView() {
        return gameView;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    @SuppressWarnings("unused")
    private void testInitialization(){
        ArrayList<Square> squares = new ArrayList<Square>();

        squares.add(new Square(SquareColor.BLACK, 1, 1));
        squares.add(new Square(SquareColor.EMPTY, 0, 2));
        squares.add(new Square(SquareColor.EMPTY, 0, 3));
        squares.add(new Square(SquareColor.EMPTY, 0, 4));
        squares.add(new Square(SquareColor.EMPTY, 0, 5));
        squares.add(new Square(SquareColor.EMPTY, 0, 6));
        squares.add(new Square(SquareColor.EMPTY, 0, 7));
        squares.add(new Square(SquareColor.EMPTY, 0, 8));
        squares.add(new Square(SquareColor.EMPTY, 0, 9));
        squares.add(new Square(SquareColor.EMPTY, 0, 10));
        squares.add(new Square(SquareColor.EMPTY, 0, 11));
        squares.add(new Square(SquareColor.EMPTY, 0, 12));
        squares.add(new Square(SquareColor.EMPTY, 0, 13));
        squares.add(new Square(SquareColor.EMPTY, 0, 14));
        squares.add(new Square(SquareColor.EMPTY, 0, 15));
        squares.add(new Square(SquareColor.EMPTY, 0, 16));
        squares.add(new Square(SquareColor.EMPTY, 0, 17));
        squares.add(new Square(SquareColor.EMPTY, 0, 18));
        squares.add(new Square(SquareColor.EMPTY, 0, 19));
        squares.add(new Square(SquareColor.EMPTY, 0, 20));
        squares.add(new Square(SquareColor.EMPTY, 0, 21));
        squares.add(new Square(SquareColor.EMPTY, 0, 22));
        squares.add(new Square(SquareColor.EMPTY, 0, 23));
        squares.add(new Square(SquareColor.WHITE, 1, 24));

        session.getCurrentGame().getBoard().initializeSquares(squares);

        squares = new ArrayList<Square>();
        squares.add(new Square(SquareColor.WHITE, 14, 25));
        squares.add(new Square(SquareColor.BLACK, 14, 0));
        session.getCurrentGame().getBoard().initializeVictorySquares(squares);

        squares = new ArrayList<Square>();
        squares.add(new Square(SquareColor.WHITE, 0, 0));
        squares.add(new Square(SquareColor.BLACK, 0, 25));
        session.getCurrentGame().getBoard().initializeBarSquares(squares);
    }

    @Override
    public Controller getController() {
        return this;
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void back() {
        // Implementation for back navigation
    }
}

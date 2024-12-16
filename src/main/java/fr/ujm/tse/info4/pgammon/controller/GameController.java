// 
//
//  @ Project : Project Gammon
//  @ File : GameController.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.controller;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.jdom2.JDOMException;

import fr.ujm.tse.info4.pgammon.models.Square;
import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.Move;
import fr.ujm.tse.info4.pgammon.models.SessionState;
import fr.ujm.tse.info4.pgammon.models.SessionManagement;
import fr.ujm.tse.info4.pgammon.models.AssistantLevel;
import fr.ujm.tse.info4.pgammon.models.Game;
import fr.ujm.tse.info4.pgammon.models.Profiles;
import fr.ujm.tse.info4.pgammon.models.Session;
import fr.ujm.tse.info4.pgammon.views.GameView;

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
        build();
        
        boardController = new BoardController(session.getCurrentGame(), gameView, this);
        gameView.getInProgressViewBottom().updateScore(session.getScores().get(session.getSessionParameters().getWhitePlayer()), 
                                                      session.getScores().get(session.getSessionParameters().getBlackPlayer()));
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
                
                Move move = session.getCurrentGame().NextMove(gameReviewPosition);
                if (move != null)
                    gameView.getCompletedViewBottom().getReplayBar().goTo(move, isForwardDirection);
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
        gameView.getInProgressViewBottom().getHelp().addMouseListener(new MouseListener() {
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
                URI uri = URI.create("http://www.google.fr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0CEoQFjAA&url=http%3A%2F%2Ffr.wikipedia.org%2Fwiki%2FBackgammon&ei=R0XUUM6YL4yHhQelkYHYBQ&usg=AFQjCNEOHnc7riItGN_di3jAPILrXp9twA&sig2=uesTfMvnLMwYI8reGb-vWw&bvm=bv.1355534169,d.ZG4&cad=rja");
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    
    public void listenerGameReviewButtons()
    {
        gameView.getCompletedViewBottom().getReplayBar().getEndBtn().addMouseListener(new MouseListener(){
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
                    Move move = session.getCurrentGame().NextMove(gameReviewPosition);
                    if (move != null)
                        gameView.getCompletedViewBottom().getReplayBar().goTo(move, isForwardDirection);
                }
                
                gameView.getCompletedViewBottom().getReplayBar().goEnd();
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });
        
        gameView.getCompletedViewBottom().getReplayBar().getBeginBtn().addMouseListener(new MouseListener(){
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

                gameView.getCompletedViewBottom().getReplayBar().goBegin();
                session.getCurrentGame().getBoard().resetSquares();
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });
        
        gameView.getCompletedViewBottom().getReplayBar().getNextBtn().addMouseListener(new MouseListener(){
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
                
                Move move = session.getCurrentGame().NextMove(gameReviewPosition);
                if (move != null)
                    gameView.getCompletedViewBottom().getReplayBar().goTo(move, isForwardDirection);
                }   
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }
        });
        
        gameView.getCompletedViewBottom().getReplayBar().getPrevBtn().addMouseListener(new MouseListener(){
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
                    Move move = session.getCurrentGame().PreviousMove(gameReviewPosition);
                    
                    if (move != null)
                        gameView.getCompletedViewBottom().getReplayBar().goTo(move, isForwardDirection);
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
                    gameView.getCompletedViewBottom().getReplayBar().setTurns(session.getCurrentGame().getPlayerTurnHistory());
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
        gameView.getRightPanelInProgress().getBack().addMouseListener(new MouseListener(){
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
    
    public void listenerRollDice()
    {
        gameView.getRightPanelInProgress().getDices().addMouseListener(new MouseListener(){
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
                if(session.getCurrentGame().isTurnFinished() && !session.getCurrentGame().isGameFinished())
                {
                    session.getCurrentGame().rollDice();
                    if (boardController.getClock() != null)
                        boardController.getClock().restart();
                    if(!session.getCurrentGame().hasPossibleMove())
                    {
                        gameView.showRequestDialog("No possible move", "");
                        boardController.changeTurn();
                    }
                }
                gameView.updateUI();
                gameView.getBoardView().updateUI();
                gameView.getBoardView().updateDice();
            }           
        });
    }
    
    public void listenerGetPossibleMovesPlayer1()
    {
        gameView.getPlayer1Panel().getPossibleMove().addMouseListener(new MouseListener() {
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
                    session.getCurrentGame().getGameParameters().getWhitePlayer().setAssistantLevel(AssistantLevel.SIMPLE);
                }
                else
                {
                    session.getCurrentGame().getGameParameters().getWhitePlayer().setAssistantLevel(AssistantLevel.NOT_USED);
                    gameView.getBoardView().setPossibleMoves(null);
                    gameView.getBoardView().updateUI();
                }
                gameView.getPlayer1Panel().updateData();
            }
        });
    }
    
    public void listenerGetPossibleMovesPlayer2()
    {
        gameView.getPlayer2Panel().getPossibleMove().addMouseListener(new MouseListener() {
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
                    session.getCurrentGame().getGameParameters().getBlackPlayer().setAssistantLevel(AssistantLevel.SIMPLE);
                }
                else
                {
                    session.getCurrentGame().getGameParameters().getBlackPlayer().setAssistantLevel(AssistantLevel.NOT_USED);
                    gameView.getBoardView().setPossibleMoves(new ArrayList<Square>());
                    gameView.getBoardView().updateUI();
                }
                gameView.getPlayer2Panel().updateData();
            }
        });
    }

    public void listenerDoubleStakeButton()
    {
        if (!session.getSessionParameters().isUsingDoubling())
        {
            gameView.getRightPanelInProgress().getDoubling().setEnabled(false);
        }
        else
        {
            gameView.getRightPanelInProgress().getDoubling().addMouseListener(new MouseListener(){
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
                        gameView.showRequestDialog("Do you accept the doubling cube ?", options).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String action = e.getActionCommand();
                                if (action == "Yes")
                                {
                                    doubleStakeColor = session.getCurrentGame().getCurrentPlayer();
                                    session.getCurrentGame().doubleStake();
                                }
                                else if (action == "No")
                                {
                                    endGame();
                                }
                                gameView.getRightPanelInProgress().updateDoubling();
                            }
                        });
                    }
                }
            });
        }
    }

    public void listenerInterruptGame()
    {
        gameView.getRightPanelReview().getWhiteX().addMouseListener(new MouseListener(){
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
                gameView.showRequestDialog("What do you want to do ?", options).addActionListener(new ActionListener() {
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
                                SessionManagement management = SessionManagement.getSessionManagement();
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
        gameView.getInProgressViewBottom().getBlackX().addMouseListener(new MouseListener(){
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
                gameView.showRequestDialog("What do you want to do ?", options).addActionListener(new ActionListener() {
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
                                SessionManagement management = SessionManagement.getSessionManagement();
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

    public void listenerNextGame()
    {
        gameView.getRightPanelReview().getNext().addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                if(session.isSessionFinished())
                {
                    controller.back();
                    ((MainController)controller).endSession();
                    Profiles profiles = Profiles.getProfiles();
                    profiles.save();
                }
                else
                    gameController.newGame();
            }
            @Override
            public void mousePressed(MouseEvent arg0) {}
            @Override
            public void mouseExited(MouseEvent arg0) {}
            @Override
            public void mouseEntered(MouseEvent arg0) {}
            @Override
            public void mouseClicked(MouseEvent arg0) {}
        });
    }

    public void newGame()
    {
        session.newGame();
        session.startGame();

        gameView.setGame(session.getCurrentGame());
        gameView.setState(SessionState.IN_PROGRESS);
        boardController = new BoardController(session.getCurrentGame(), gameView, this);

        session.startGame();
        gameView.updateUI();
    }

    public void endGame()
    {
        if (boardController.getClock() != null)
        {
            boardController.getClock().stop();
            boardController.getClock().setValue(0);
        }

        session.endGame();

        gameView.getInProgressViewBottom().updateScore(
                session.getScores().get(session.getSessionParameters().getWhitePlayer()),
                session.getScores().get(session.getSessionParameters().getBlackPlayer())
        );

        if(session.checkSessionEnd())
        {
            session.endSession();
            gameView.getRightPanelReview().getNextLabel().setText("<html>Finish<br>Session</html>");
            gameView.showRequestDialog(
                    session.getCurrentGame().getGameParameters().getPlayer(session.getCurrentGame().getCurrentPlayer()).getUsername(),
                    " wins the session!"
            );
        }
        else
        {
            gameView.showRequestDialog(
                    session.getCurrentGame().getGameParameters().getPlayer(session.getCurrentGame().getCurrentPlayer()).getUsername(),
                    " wins the game!"
            );
        }
        gameView.setState(SessionState.COMPLETED);
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
        session.getCurrentGame().getBoard().initializeWinningSquares(squares);

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

package com.HyenaBgammon.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;

import com.HyenaBgammon.exception.TurnNotPlayableException;
import com.HyenaBgammon.view.CaseButton;
import com.HyenaBgammon.models.Square;
import com.HyenaBgammon.models.SixSidedDie;
import com.HyenaBgammon.models.Clock;
import com.HyenaBgammon.models.ClockEvent;
import com.HyenaBgammon.models.ClockEventListener;
import com.HyenaBgammon.models.AssistantLevel;
import com.HyenaBgammon.models.Game;
import com.HyenaBgammon.models.Board;
import com.HyenaBgammon.view.GameView;
import com.HyenaBgammon.view.BoardView;

public class BoardController implements Controller {
    private Board board;
    private Game game;
    private BoardView boardView;
    private GameView gameView;
    private Clock Clock;
    
    private GameController gameController;
    private JFrame frame;

    public BoardController(Game game, GameView gameView, GameController gameController) {
        this.game = game;
        this.board = game.getBoard();
        this.gameView = gameView;
        this.boardView = gameView.getBoardView();
        this.gameController = gameController;
    
        build();
        boardView.updateDice();
        gameView.displayTransition(game.getGameParameters().getPlayer(game.getCurrentPlayer()).getUsername(), "Player" + game.getCurrentPlayer().toString());    
    }

    private void build() {
        buildClock();
        listenerCaseButton();
    }
    
    private void listenerCaseButton() {
        Collection<CaseButton> cases = boardView.getSquareButtons();
        for (CaseButton caseButton : cases) {
            caseButton.addMouseListener(new MouseListener() {
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
                    CaseButton caseButton = (CaseButton) e.getSource();
                    if (!game.isTurnFinished() && !game.isGameFinished())
                        if(boardView.getCandidate() == null 
                                && game.getCurrentPlayer() == caseButton.getCase().getCheckerColor()) {
                            if (caseButton.getCase().getNumCheckers() != 0 
                                    && (!board.isPieceOnBar(game.getCurrentPlayer()) 
                                            || caseButton.getCase().isBarSquare())
                                            && caseButton.getCase().getCheckerColor() == game.getCurrentPlayer()) {
                                boardView.setCandidate(caseButton);
                                if(game.getGameParameters().getPlayer(game.getCurrentPlayer()).getAssistantLevel() == AssistantLevel.BASIC
                                        || game.getGameParameters().getPlayer(game.getCurrentPlayer()).getAssistantLevel() == AssistantLevel.FULL)
                                    boardView.setPossible(game.getPossibleMoves(caseButton.getCase()));
                            }
                            else if (board.isPieceOnBar(game.getCurrentPlayer())) {
                                gameView.displayRequestWindow("Warning!", "Remove hit pieces before playing.");
                            }
                        }
                        else if (boardView.getCandidate() != null) {
                            if (game.playMove(boardView.getCandidate().getCase(), caseButton.getCase())) {
                                boardView.unCandidateAll();
                                boardView.setPossible(new ArrayList<Square>());
                                
                                if(game.isGameFinished()) {
                                    gameController.endGame();
                                }
                                
                                if (game.areDiceUsed()) {    
                                    changeTurn();    
                                }
                                else if(!game.hasPossibleMove()) {
                                    changeTurn();
                                    game.rollDice();
                                    if(!game.hasPossibleMove()) {
                                        gameView.displayRequestWindow("No possible move", "");
                                        changeTurn();
                                    }
                                }
                            }
                            else {
                                boardView.unCandidateAll();
                                boardView.setPossible(new ArrayList<Square>());
                            }
                        }

                    boardView.updateUI();
                    boardView.updateDice();
                }
            });
        }
    }
    
    public void changeTurn() {
        if (Clock != null) {
            Clock.stop();
            Clock.setValue(0);
        }
        game.changeTurn();
        gameView.displayTransition(game.getGameParameters().getPlayer(game.getCurrentPlayer()).getUsername(), "Player" + game.getCurrentPlayer().toString());
    }
    
    private void buildClock() {
        if (game.getGameParameters().getSecondsPerTurn() != 0) {
            // Initialize the clock with the time per turn
            Clock = new Clock(game.getGameParameters().getSecondsPerTurn());

            // Add a listener to manage clock events
            Clock.addListener(new ClockEventListener() {
                @Override
                public void updateClock(ClockEvent evt) {
                    // Optional: Update clock display or logic here if needed
                }

                @Override
                public void clockEnd(ClockEvent evt) {
                    try {
                        // Count remaining unused dice
                        int unusedDiceCount = 0;
                        for (SixSidedDie die : game.getSixSidedDie()) {
                            if (!die.isUsed()) {
                                unusedDiceCount++;
                            }
                        }

                        // Perform random moves for remaining unused dice
                        for (int i = 0; i < unusedDiceCount; i++) {
                            game.randomMove();
                        }

                    } catch (TurnNotPlayableException e) {
                        // If no moves can be played, force a turn change
                        changeTurn();
                    }

                    // Clear candidate moves and update board state
                    boardView.unCandidateAll();
                    boardView.setPossible(new ArrayList<Square>());

                    // Logic to handle dice usage and possible moves
                    if (game.areDiceUsed()) {
                        changeTurn();
                    } else if (!game.hasPossibleMove()) {
                        gameView.displayRequestWindow("No possible move", "");
                        changeTurn();
                    }

                    // Refresh the board and dice views
                    boardView.updateUI();
                    boardView.updateDice();
                }
            });
        } else {
            Clock = null; // If no time limit is set, disable the clock
        }

        // Attach the clock to the game view's clock bar
        gameView.getClockBar().setClock(Clock);
    }

    /**
     * Returns the Clock
     * @return Clock
     */
    public Clock getClock() {
        return Clock;
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
    public void back() {}
}

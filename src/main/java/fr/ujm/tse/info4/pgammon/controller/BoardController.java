package fr.ujm.tse.info4.pgammon.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;

import fr.ujm.tse.info4.pgammon.exception.TurnNotPlayableException;
import fr.ujm.tse.info4.pgammon.gui.CaseButton;
import fr.ujm.tse.info4.pgammon.models.Square;
import fr.ujm.tse.info4.pgammon.models.SixSidedDie;
import fr.ujm.tse.info4.pgammon.models.Clock;
import fr.ujm.tse.info4.pgammon.models.ClockEvent;
import fr.ujm.tse.info4.pgammon.models.ClockEventListener;
import fr.ujm.tse.info4.pgammon.models.AssistantLevel;
import fr.ujm.tse.info4.pgammon.models.Game;
import fr.ujm.tse.info4.pgammon.models.Board;
import fr.ujm.tse.info4.pgammon.views.GameView;
import fr.ujm.tse.info4.pgammon.views.BoardView;

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
        gameView.showTransition(game.getGameParameters().getPlayer(game.getCurrentPlayer()).getNickname(), "Player" + game.getCurrentPlayer().toString());    
    }

    private void build() {
        buildClock();
        listenerCaseButton();
    }
    
    private void listenerCaseButton() {
        Collection<CaseButton> cases = boardView.getCasesButtons();
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
                                && game.getCurrentPlayer() == caseButton.getCase().getPieceColor()) {
                            if (caseButton.getCase().getPieceCount() != 0 
                                    && (!board.isPieceInBar(game.getCurrentPlayer()) 
                                            || caseButton.getCase().isBar())
                                            && caseButton.getCase().getPieceColor() == game.getCurrentPlayer()) {
                                boardView.setCandidate(caseButton);
                                if(game.getGameParameters().getPlayer(game.getCurrentPlayer()).getAssistantLevel() == AssistantLevel.SIMPLE
                                        || game.getGameParameters().getPlayer(game.getCurrentPlayer()).getAssistantLevel() == AssistantLevel.COMPLETE)
                                    boardView.setPossibleMoves(game.getPossibleMoves(caseButton.getCase()));
                            }
                            else if (board.isPieceInBar(game.getCurrentPlayer())) {
                                gameView.showRequestWindow("Warning!", "Remove hit pieces before playing.");
                            }
                        }
                        else if (boardView.getCandidate() != null) {
                            if (game.playMove(boardView.getCandidate().getCase(), caseButton.getCase())) {
                                boardView.uncandidateAll();
                                boardView.setPossibleMoves(new ArrayList<Case>());
                                
                                if(game.isGameFinished()) {
                                    gameController.endGame();
                                }
                                
                                if (game.areDiceUsed()) {    
                                    changeTurn();    
                                }
                                else if(!game.hasValidMove()) {
                                    changeTurn();
                                    game.rollDice();
                                    if(!game.hasValidMove()) {
                                        gameView.showRequestWindow("No possible move", "");
                                        changeTurn();
                                    }
                                }
                            }
                            else {
                                boardView.uncandidateAll();
                                boardView.setPossibleMoves(new ArrayList<Case>());
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
        gameView.showTransition(game.getGameParameters().getPlayer(game.getCurrentPlayer()).getNickname(), "Player" + game.getCurrentPlayer().toString());
    }
    
    private void buildClock() {
        if (game.getGameParameters().getSecondsPerTurn() != 0) {
            Clock = new Clock(game.getGameParameters().getSecondsPerTurn());
            Clock.addListener(new ClockEventListener() {
                @Override
                public void updateClock(ClockEvent Clock) {}
                
                @Override
                public void endClock(ClockEvent evt) {
                    try {
                        // Random movement for remaining dice
                        int unusedDiceCount = 0;
                        for (SixSidedDie die : game.getSixSidedDie()) {
                            if(!die.isUsed())
                                unusedDiceCount++;
                        }
                        for(int i = 0; i < unusedDiceCount; i++) {
                            game.randomMove();
                        }
                    } catch (UnplayableTurnException e) {
                        changeTurn();
                    }
                    boardView.uncandidateAll();
                    boardView.setPossibleMoves(new ArrayList<Case>());
                    
                    if (game.areDiceUsed()) {    
                        changeTurn();            
                    }
                    else if(!game.hasValidMove()) {
                        gameView.showRequestWindow("No possible move", "");
                        changeTurn();
                    }    
                    
                    boardView.updateUI();
                    boardView.updateDice();
                }
            });
        }
        else {
            Clock = null;    
        }
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

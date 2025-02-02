package com.HyenaBgammon.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;

import com.HyenaBgammon.exception.TurnNotPlayableException;
import com.HyenaBgammon.view.CaseButton;
import com.HyenaBgammon.models.Square;
import com.HyenaBgammon.models.SixSidedDie;
import com.HyenaBgammon.models.Clock;
import com.HyenaBgammon.models.ClockEvent;
import com.HyenaBgammon.models.ClockEventListener;
import com.HyenaBgammon.models.DieType;
import com.HyenaBgammon.models.AssistantLevel;
import com.HyenaBgammon.models.Game;
import com.HyenaBgammon.models.Question;
import com.HyenaBgammon.models.QuestionManager;
import com.HyenaBgammon.models.Board;
import com.HyenaBgammon.view.GameView;
import com.HyenaBgammon.view.BoardView;

public class BoardController implements Controller {
    private Board board;
    private Game game;
    private BoardView boardView;
    private GameView gameView;
    private Clock Clock;
    private boolean turnChanged = false; // Add a flag to track turn changes
    private static List<CaseButton> allCaseButtons;
    
    private GameController gameController;
    private JFrame frame;
    private QuestionManager questionManager; // Added QuestionManager

    public BoardController(Game game, GameView gameView, GameController gameController) {
        this.game = game;
        this.board = game.getBoard();
        this.gameView = gameView;
        this.boardView = gameView.getBoardView();
        this.gameController = gameController;
        this.questionManager = QuestionManagerFactory.createQuestionManager();

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
                    turnChanged = false; // Reset the flag at the start of the turn
                    if (game.isQuestionRequiredAtTurnStart()) {
                    	checkTurnStartLogic();
                        return; // Don't proceed until the question is answered
                    }
                    CaseButton caseButton = (CaseButton) e.getSource();
                    if (!game.isTurnFinished() && !game.isGameFinished())
                        if (boardView.getCandidate() == null 
                                && game.getCurrentPlayer() == caseButton.getCase().getCheckerColor()) {
                        	
                            if (caseButton.getCase().getNumCheckers() != 0 
                                    && (!board.isPieceOnBar(game.getCurrentPlayer()) 
                                            || caseButton.getCase().isBarSquare())
                                    && caseButton.getCase().getCheckerColor() == game.getCurrentPlayer()) {
                                boardView.setCandidate(caseButton);
                                if (game.getGameParameters().getPlayer(game.getCurrentPlayer()).getAssistantLevel() == AssistantLevel.BASIC
                                        || game.getGameParameters().getPlayer(game.getCurrentPlayer()).getAssistantLevel() == AssistantLevel.FULL)
                                    boardView.setPossible(game.getPossibleMoves(caseButton.getCase()));
                            } else if (board.isPieceOnBar(game.getCurrentPlayer())) {
                                gameView.displayRequestWindow("Warning!", "Remove hit pieces before playing.");
                            }
                        } else if (boardView.getCandidate() != null) {
                            if (game.playMove(boardView.getCandidate().getCase(), caseButton.getCase())) {
                                // Check the type of the triangle where the piece was moved
                                String triangleType = boardView.getTriangleType(caseButton.getCase());
                                
                                if (triangleType.equals("SURPRISE") && !caseButton.getCase().isActivated() && !game.isSurpriseStationHit()) {
                                    // Activate the surprise station
                                    game.setSurpriseStationEnabled(true); // Enable extra turn for the same player
                                    game.setSurpriseStationHit(true);    // Ensure it triggers only once

                                    // Notify the player
                                    System.out.println("Surprise station triggered! Extra turn will be granted.");
                                    gameView.displayRequestWindow("Surprise Station!", "You earned an extra turn!");
                                }
                                if (triangleType.equals("QUESTION")) {
                                    handleQuestionTriangle(caseButton);
                                    turnChanged=true; 
                                }


                                if (!triangleType.equals("NORMAL")) {
                                    System.out.println("This is a " + triangleType + " triangle.");
                                }
                                

                                boardView.unCandidateAll();
                                boardView.setPossible(SquareListFactory.createSquareList());

                                if (game.isGameFinished()) {
                                    gameController.endGame();
                                }
                                if(!turnChanged) {
                                if (game.areDiceUsed()) {    
                                    changeTurn(); 
                                    turnChanged= true; 
                                } else if (!game.hasPossibleMove()) {
                                    changeTurn();
                                    turnChanged=true;
                                    game.rollDice();
                                    if (!game.hasPossibleMove()) {
                                        gameView.displayRequestWindow("No possible move", "");
                                        changeTurn();
                                        turnChanged =true; 
                                    }
                                }
                            }
                            }else {
                                boardView.unCandidateAll();
                                boardView.setPossible(SquareListFactory.createSquareList());
                            }
                        }

                    boardView.updateUI();
                    boardView.updateDice();
                }
            });
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////to do q
    public void handleDicePress(SixSidedDie die) {
        if (die.getDieType() == DieType.QUESTION) {
            String questionDifficulty = die.getQuestionDifficulty();
            gameView.displayRequestWindow("Question Difficulty", "Answer a " + questionDifficulty + " question!");

            // Add logic to fetch and display the question in the future
        } else {
            gameView.displayRequestWindow("Dice Value", "Move pieces based on dice value: " + die.getValue());
        }

        die.use();
        boardView.updateDice(); // Refresh dice state
    }
    
    private void handleTurnStartQuestion() {
        lockGame(); // Lock the game while the question is displayed

        // Fetch a random question
        Question question = questionManager.getRandomQuestion();
        if (question == null) {
            gameView.displayRequestWindow("No Question Available", "Continue your turn.");
            game.setQuestionRequiredAtTurnStart(false); // Allow the turn to proceed
            unlockGame(); // Unlock inputs if no question is available
            return;
        }

        // Display the question and handle the result
        gameView.displayQuestion(question, 30, (isCorrect) -> {
            if (isCorrect) {
                gameView.displayColoredRequestWindow("Correct!", "You answered correctly. Begin your turn.", "green");
                game.setQuestionRequiredAtTurnStart(false); // Allow the turn to proceed
                if (!game.hasPossibleMove()) {
                    gameView.displayRequestWindow("No Possible Moves", "Your turn is over. Changing turn...");
                    changeTurn(); // End the turn if no moves are available
                }
                else {
                unlockGame(); // Unlock inputs for the turn
                }
            } else {
                gameView.displayColoredRequestWindow("Wrong!", "Time's up or wrong answer. Turn finished.", "red");
                game.setQuestionRequiredAtTurnStart(false); // Clear the flag
                changeTurn(); // End the turn
                unlockGame(); // Unlock inputs for the next player
            }
        });
    }

    // Call this method when the turn starts
    public void checkTurnStartLogic() {
        if (game.isQuestionRequiredAtTurnStart()) {
            handleTurnStartQuestion(); // Handle the question if required
        }
    }

    private void handleQuestionTriangle(CaseButton caseButton) {
        // Lock the game while displaying the question
        lockGame();

        // Fetch a random question
        Question question = questionManager.getRandomQuestion();

        if (question == null) {
            // Handle the case where no questions are available
            gameView.displayRequestWindow("No Question Available", "Continue your turn.");
            unlockGame();
            return;
        }

        // Pass the question object to GameView for display
        gameView.displayQuestion(question, 30, (isCorrect) -> {
            if (isCorrect) {
                // Notify the player they answered correctly with green text
                gameView.displayColoredRequestWindow("Correct!", "You answered correctly. Continue your turn.", "green");
	                if (!game.hasPossibleMove()) {
	                    gameView.displayRequestWindow("No Possible Moves", "Your turn is over. Changing turn...");
	                    changeTurn(); // End the turn if no moves are available
	                }else {
		                System.out.print("Test to correct");
		                unlockGame(); // Unlock the game so the player can continue their turn
	                	}
            } else {
                // Notify the player they answered incorrectly or timed out with red text
                gameView.displayColoredRequestWindow("Wrong!", "Time's up or wrong answer. Turn finished.", "red");
                System.out.print("Test to  notttttttt correct");
                changeTurn(); // End the turn
                unlockGame(); // Unlock the game after ending the turn
            }
        });
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
                    boardView.setPossible(SquareListFactory.createSquareList());

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
    // check player , player position ! so i can see if it , what type of the tringle , 
    public void checkPlayerPosition(Square playerSquare) {
        if (playerSquare == null) {
            System.out.println("Player is not on a valid square.");
            return;
        }

        String triangleType = boardView.getTriangleType(playerSquare);

        switch (triangleType) {
            case "QUESTION":
                System.out.println("Player is in a Question Mark triangle!");
                break;
            case "SURPRISE":
                System.out.println("Player is in a Surprise triangle!");
                break;
            default:
                System.out.println("Player is in a normal triangle.");
                break;
        }
    }
    public void changeTurn() {
        if (Clock != null) {
            Clock.stop();
            Clock.setValue(0);
        }

        // Change the current player in the game
        game.changeTurn();

        // Log the current player for debugging
        System.out.println("Turn changed to Player: " + game.getCurrentPlayer());

        // Update the game view with the new player's turn
        gameView.displayTransition(
            game.getGameParameters().getPlayer(game.getCurrentPlayer()).getUsername(),
            "Player " + game.getCurrentPlayer().toString()
        );
    }

    

    private void lockGame() {
        boardView.setEnabled(false); // Disable the board interaction
        gameView.lockInputs();      // Assume lockInputs disables all UI components
    }

    private void unlockGame() {
        boardView.setEnabled(true);  // Enable the board interaction
        gameView.unlockInputs();     // Assume unlockInputs re-enables all UI components
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
    public void refreshMarkers() {
        boardView.updateMarkers();
    }

    @Override
    public void back() {}
    
}

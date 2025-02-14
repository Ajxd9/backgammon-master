package com.HyenaBgammon.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import com.HyenaBgammon.exception.TurnNotPlayableException;
import com.HyenaBgammon.view.CaseButton;

public class Game extends AbstractGame {
    private GameParameters gameParameters;
    private DoublingCube doublingCube;
    private ArrayList<SixSidedDie> SixSidedDie;
    private Board board;
    private SquareColor firstPlayer;
    private SquareColor currentPlayer;
    private ArrayList<Turn> playerTurnHistory;
    private int gameId;
    private boolean gameFinished;
    private boolean turnFinished;
    private int diceUsed;
    private GameDifficulty gameDifficulty;
    private String colorSet = "Black & White";
    private boolean surpriseStationEnabled = false; // Tracks if a surprise station is active
    private boolean surpriseStationHit = false;    // Ensures surprise station is triggered only once
    private boolean questionRequiredAtTurnStart = false; // New field
    private boolean gameOver;

    private LocalDateTime startTime;
   

    private boolean skipNextTurn = false; // Tracks if the next turn should be skipped

    /**
     *
     * @param p
     */
    
    public Game(GameParameters p) {
        this.gameParameters = p;
        this.board = new Board();
        this.doublingCube = DoublingCubeFactory.createDoublingCube();
        this.SixSidedDie = SixSidedDieListFactory.createSixSidedDieList();
        this.playerTurnHistory.add(new Turn(currentPlayer, SixSidedDie));

        // Initialize game state variables
        this.turnFinished = true;
        this.gameFinished = false;

        // Initialize game difficulty from GameParameters
        this.gameDifficulty = p.getDifficulty(); // Ensure GameParameters has a valid difficulty set
        this.colorSet = p.getCheckerColorSet();
        System.out.println(colorSet);
        CaseButton.loadCheckerImages(colorSet);
    }

    public Game(int gameId, GameParameters p) {
        this.gameId = gameId;
        this.gameParameters = p;

        this.board = new Board();
        this.doublingCube = new DoublingCube();

        this.playerTurnHistory = new ArrayList<>();
        this.SixSidedDie = new ArrayList<>();

        // Initialize game state variables
        this.turnFinished = true;
        this.gameFinished = false;

        // Initialize game difficulty from GameParameters
        this.gameDifficulty = p.getDifficulty();
        if(p.getCheckerColorSet() == null) {
        	this.colorSet = "Black & White";
        }else {
        this.colorSet = p.getCheckerColorSet();
        }
        System.out.println(colorSet);
        CaseButton.loadCheckerImages(colorSet);

        System.out.println("The game you just started is: " + this.gameParameters.getDifficulty());
    }
    
    
    public Game(GameParameters gameParameters, DoublingCube doublingCube,
			ArrayList<com.HyenaBgammon.models.SixSidedDie> sixSidedDie, Board board, SquareColor firstPlayer,
			SquareColor currentPlayer, ArrayList<Turn> playerTurnHistory, int gameId, boolean gameFinished,
			boolean turnFinished, int diceUsed, GameDifficulty gameDifficulty, boolean surpriseStationEnabled,
			boolean surpriseStationHit, boolean questionRequiredAtTurnStart, LocalDateTime startTime,
			boolean skipNextTurn) {
		super();
		this.gameParameters = gameParameters;
		this.doublingCube = doublingCube;
		SixSidedDie = sixSidedDie;
		this.board = board;
		this.firstPlayer = firstPlayer;
		this.currentPlayer = currentPlayer;
		this.playerTurnHistory = playerTurnHistory;
		this.gameId = gameId;
		this.gameFinished = gameFinished;
		this.turnFinished = turnFinished;
		this.diceUsed = diceUsed;
		this.gameDifficulty = gameDifficulty;
		this.surpriseStationEnabled = surpriseStationEnabled;
		this.surpriseStationHit = surpriseStationHit;
		this.questionRequiredAtTurnStart = questionRequiredAtTurnStart;
		this.startTime = startTime;
		this.skipNextTurn = skipNextTurn;
	}

    public void startGameTime() {
        this.startTime = LocalDateTime.now(); // Set the start time
    }
    public LocalDateTime getStartTime() {
        return this.startTime;
    }
    
    
    /**
     *
     */
    public void startFirstGame() {
        gameFinished = false;
        chooseFirstPlayerGameStart();
    }

    /**
     *
     * @param player
     */
    public void startNewGame(SquareColor player) {
        gameFinished = false;

        if(player == SquareColor.WHITE)
            currentPlayer = SquareColor.BLACK;
        else
            currentPlayer = SquareColor.WHITE;

        firstPlayer = currentPlayer;
    }

    /**
     *
     */
    public void beginTurn() {
        playerTurnHistory.add(new Turn(currentPlayer, SixSidedDie));
    }

    /**

     Abed Note: here I can add the option for Surprise Stations
     */
    public void changeTurn() {
        if (board.isAllPiecesMarked(currentPlayer)) {
            endGame();
            return; // Exit to prevent further changes after the game ends
        }

        if (surpriseStationHit && surpriseStationEnabled) {
            // Keep the current player if the surprise station grants an extra turn
            surpriseStationEnabled = false; // Reset the extra turn flag
            turnFinished = false; // Reset the turn finished flag
        } else {
            // Normal turn change
            if (currentPlayer == SquareColor.WHITE) {
                currentPlayer = SquareColor.BLACK;
            } else {
                currentPlayer = SquareColor.WHITE;
            }
        }

        SixSidedDie = new ArrayList<SixSidedDie>();
        turnFinished = true;
    }


    /**
     *
     */
    @Override
    protected void endGame() {
        System.out.println("Game over! Determining the winner...");
        gameFinished = true;

        // Determine the winner based on game state
        SquareColor winner = (board.isAllPiecesMarked(SquareColor.WHITE)) ? SquareColor.WHITE : SquareColor.BLACK;
        System.out.println("The winner is: " + winner);

        // Clean up resources
        cleanupGame();
    }

    private void cleanupGame() {
        System.out.println("Cleaning up game resources...");
        playerTurnHistory.clear();
        SixSidedDie.clear();
    }

    /**
     *
     */
    public void chooseFirstPlayerGameStart() {
        ArrayList<SixSidedDie> choiceDice = new ArrayList<SixSidedDie>();
        choiceDice.add(new SixSidedDie(currentPlayer));
        choiceDice.add(new SixSidedDie(currentPlayer));

        if(choiceDice.get(0).getValue() == choiceDice.get(1).getValue()) {
            chooseFirstPlayerGameStart();
        }
        else if(choiceDice.get(0).getValue() > choiceDice.get(1).getValue())
            currentPlayer = SquareColor.WHITE;
        else
            currentPlayer = SquareColor.BLACK;

        firstPlayer = currentPlayer;
    }

    /**
     *
     * @param startSquareInt
     * @param endSquareInt
     * @return
     */
    public boolean playMove(int startSquareInt, int endSquareInt) {
        Move move = board.intToMove(startSquareInt, endSquareInt, currentPlayer);
        return playMove(move.getStartSquare(), move.getEndSquare());
    }

    /**
     *
     * @param startSquare
     * @param endSquare
     * @return
     */
    public boolean playMove(Square startSquare, Square endSquare) {
        if(isMovePossible(startSquare, endSquare)) {
            SquareColor enemyPlayer = (currentPlayer == SquareColor.WHITE) ?
                    SquareColor.BLACK : SquareColor.WHITE;

            int piecesOnBar = board.getBarSquare(enemyPlayer).getNumCheckers();

            if (board.movePiece(startSquare, endSquare)) {
                SixSidedDie.get(diceUsed).use();

                getLastTurn().addMovement(new Movement(startSquare, endSquare,
                        (piecesOnBar < board.getBarSquare(enemyPlayer).getNumCheckers())));

                if (board.isAllPiecesMarked(currentPlayer))
                    endGame();
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean playMove(Move move) {
        return playMove(move.getStartSquare(), move.getEndSquare());
    }
    /**
     *
     * @param pieceSquare
     * @param dice
     * @return
     */
    public boolean canMarkThisPiece(Square pieceSquare, SixSidedDie dice) {
        Square victorySquare = board.getVictorySquare(currentPlayer);

        if (((board.distanceBetweenSquares(pieceSquare, victorySquare) == dice.getValue()
                && currentPlayer == SquareColor.WHITE)
                || (board.distanceBetweenSquares(pieceSquare, victorySquare) == -dice.getValue()
                && currentPlayer == SquareColor.BLACK))
                && !dice.isUsed()) {
            return true;
        }
        else if (((board.distanceBetweenSquares(pieceSquare, victorySquare) < dice.getValue()
                && currentPlayer == SquareColor.WHITE && !board.isSquareBefore(pieceSquare))
                || (board.distanceBetweenSquares(pieceSquare, victorySquare) > -dice.getValue()
                && currentPlayer == SquareColor.BLACK) && !board.isSquareBefore(pieceSquare))
                && !dice.isUsed()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param pieceSquare
     * @return
     */
    public boolean canMarkThisPiece(Square pieceSquare) {
        boolean isDiceAvailable = false;
        @SuppressWarnings("unused")
        int currentDiceUsed = 0;
        Square victorySquare;

        victorySquare = board.getVictorySquare(currentPlayer);

        for (int i = 0; i < SixSidedDie.size(); i++) {
            if (((board.distanceBetweenSquares(pieceSquare, victorySquare) == SixSidedDie.get(i).getValue()
                    && currentPlayer == SquareColor.WHITE)
                    || (board.distanceBetweenSquares(pieceSquare, victorySquare) == -SixSidedDie.get(i).getValue()
                    && currentPlayer == SquareColor.BLACK))
                    && !SixSidedDie.get(i).isUsed()) {
                isDiceAvailable = true;
                currentDiceUsed = i;
            }
            else if (((board.distanceBetweenSquares(pieceSquare, victorySquare) > SixSidedDie.get(i).getValue()
                    && currentPlayer == SquareColor.WHITE && !board.isSquareBefore(pieceSquare))
                    || (board.distanceBetweenSquares(pieceSquare, victorySquare) > -SixSidedDie.get(i).getValue()
                    && currentPlayer == SquareColor.BLACK) && !board.isSquareBefore(pieceSquare))
                    && !SixSidedDie.get(i).isUsed()) {
                isDiceAvailable = true;
                currentDiceUsed = i;
            }
        }
        if (!isDiceAvailable)
            return false;

        return true;
    }

    /**
     *
     * @param startSquare
     * @param endSquare
     * @return
     */
    public boolean isMovePossible(Square startSquare, Square endSquare) {
        boolean isDiceAvailable = false;
        diceUsed = 0;

        for (int i = 0; i < SixSidedDie.size(); i++) {
            if(canMarkThisPiece(startSquare, SixSidedDie.get(i)) &&
                    endSquare.isHomeSquare() &&
                    endSquare.getCheckerColor() == startSquare.getCheckerColor()) {
                isDiceAvailable = true;
                diceUsed = i;
            }
            else if (((board.distanceBetweenSquares(startSquare, endSquare) == SixSidedDie.get(i).getValue()
                    && currentPlayer == SquareColor.WHITE)
                    || (board.distanceBetweenSquares(startSquare, endSquare) == -SixSidedDie.get(i).getValue()
                    && currentPlayer == SquareColor.BLACK))
                    && !SixSidedDie.get(i).isUsed()) {
                isDiceAvailable = true;
                diceUsed = i;
            }
        }

        if (!isDiceAvailable)
            return false;

        if (board.isMovementDirectionCorrect(startSquare, endSquare)) {
            return board.isMovePossible(startSquare, endSquare);
        }
        else
            return false;
    }

    /**
     *
     * @param startSquare
     * @return
     */
    public boolean isMovePossible(Square startSquare) {
        boolean possible = false;
        for (SixSidedDie dice : SixSidedDie) {
            if(!dice.isUsed() && isMovePossible(startSquare, board.getSquareAtDistance(startSquare, dice)))
                possible = true;
        }
        return possible;
    }

    /**
     *
     * @return
     */
    public boolean areDiceUsed() {
        for (SixSidedDie dice : SixSidedDie) {
            if (!dice.isUsed())
                return false;
        }
        return true;
    }
    /**
     *
     */
    public void rollDice() {
        SixSidedDie = new ArrayList<SixSidedDie>();
        if (gameDifficulty.equals(GameDifficulty.MEDIUM) || gameDifficulty.equals(GameDifficulty.HARD)) {
            questionRequiredAtTurnStart = true; // Mark that a question is required at the start of the turn
        }
         if(this.gameParameters.getDifficulty().equals(GameDifficulty.EASY)) {
        	
	        SixSidedDie.add(new SixSidedDie(DieType.REGULAR,currentPlayer));
	        SixSidedDie.add(new SixSidedDie(DieType.REGULAR,currentPlayer));
	        if (SixSidedDie.get(0).getValue() == SixSidedDie.get(1).getValue()) {
	            SixSidedDie.add(new SixSidedDie(SixSidedDie.get(0).getValue(), currentPlayer, DieType.REGULAR));
	            SixSidedDie.add(new SixSidedDie(SixSidedDie.get(0).getValue(), currentPlayer, DieType.REGULAR));
	        }
	        turnFinished = false;
	        beginTurn();
	       
        }   
        else if(this.gameParameters.getDifficulty().equals(GameDifficulty.MEDIUM)) {
        	
	        SixSidedDie.add(new SixSidedDie(DieType.REGULAR,currentPlayer));
	        SixSidedDie.add(new SixSidedDie(DieType.REGULAR,currentPlayer));
	        SixSidedDie.add(new SixSidedDie(DieType.QUESTION,currentPlayer));
	        if (SixSidedDie.get(0).getValue() == SixSidedDie.get(1).getValue()) {
	            SixSidedDie.add(new SixSidedDie(SixSidedDie.get(0).getValue(), currentPlayer, DieType.REGULAR));
	            SixSidedDie.add(new SixSidedDie(SixSidedDie.get(0).getValue(), currentPlayer, DieType.REGULAR));
	        }
	        turnFinished = false;
	        beginTurn();
	        
        } else if(this.gameParameters.getDifficulty().equals(GameDifficulty.HARD)) {
	        SixSidedDie.add(new SixSidedDie(DieType.ENHANCED,currentPlayer));
	        SixSidedDie.add(new SixSidedDie(DieType.ENHANCED,currentPlayer));
	        SixSidedDie.add(new SixSidedDie(DieType.QUESTION,currentPlayer));
	        if (SixSidedDie.get(0).getValue() == SixSidedDie.get(1).getValue()) {
	            SixSidedDie.add(new SixSidedDie(SixSidedDie.get(0).getValue(), currentPlayer, DieType.ENHANCED));
	            SixSidedDie.add(new SixSidedDie(SixSidedDie.get(0).getValue(), currentPlayer, DieType.ENHANCED));
	        }
	        turnFinished = false;
	        beginTurn();
        }
    }

    public void doubleDoublingCube() {
        doublingCube.doubleValue();
    }

    /**
     *
     * @throws TurnNotPlayableException
     */
    public void randomMove() throws TurnNotPlayableException {
        List<Move> possibleMoves = getPossibleMoves();
        if (possibleMoves.size() != 0)
            playMove(possibleMoves.get((int)(Math.random() * possibleMoves.size())));
        else
            throw new TurnNotPlayableException("No possible moves available");
    }

    /**
     *
     * @param square
     * @return
     */
    public List<Square> getPossibleMoves(Square square) {
        ArrayList<Square> returnSquares = new ArrayList<Square>();
        Square endSquare;
        for (SixSidedDie dice : SixSidedDie) {
            if (!dice.isUsed()) {
                endSquare = board.getSquareAtDistance(square, dice);
                if(isMovePossible(square, endSquare)) {
                    returnSquares.add(endSquare);
                }
            }
        }
        return returnSquares;
    }

    /**
     *
     * @return
     */
    public List<Move> getPossibleMoves() {
        List<Move> singleDiceMoves = new ArrayList<Move>();

        for (Square pieceSquare : board.getAllSquares()) {
            if(pieceSquare.getCheckerColor() == currentPlayer) {
                for (SixSidedDie tmpDice : SixSidedDie) {
                    for (Square tmpSquare : getPossibleMoves(pieceSquare)) {
                        singleDiceMoves.add(new Move(pieceSquare, tmpSquare));
                    }
                }
            }
        }
        return singleDiceMoves;
    }

    /**
     *
     * @return
     */
    public boolean hasPossibleMove() {
        for (Square pieceSquare : board.getAllSquares()) {
            if((!board.isPieceOnBar(currentPlayer) && pieceSquare.getCheckerColor() == currentPlayer)
                    || pieceSquare.isBarSquare())
                for (SixSidedDie dice : SixSidedDie) {
                    if(!dice.isUsed())
                        if(isMovePossible(pieceSquare, board.getSquareAtDistance(pieceSquare, dice)))
                            return true;
                }
        }
        return false;
    }
    /**
     *
     */
    public void undoLastMove() {
        Movement lastMovement;

        Turn lastTurn = getLastTurn();
        if (lastTurn != null)
            lastMovement = getLastTurn().getLastMovement();
        else
            lastMovement = null;

        if (lastMovement != null) {
            for (SixSidedDie dice : lastTurn.getSixSidedDie()) {
                if (dice.isUsed() && dice.getValue() == Math.abs(board.distanceBetweenSquares(
                        lastMovement.getEndSquare(), lastMovement.getStartSquare()))) {

                    SquareColor squareArrivedSaveColor;
                    if (lastMovement.getEndSquare().getCheckerColor() == SquareColor.WHITE)
                        squareArrivedSaveColor = SquareColor.BLACK;
                    else
                        squareArrivedSaveColor = SquareColor.WHITE;

                    board.movePiece(lastMovement.getEndSquare(), lastMovement.getStartSquare());
                    dice.resetUse();
                    if(getLastTurn().getLastMovement().isHitMove()) {
                        board.movePiece(board.getBarSquare(squareArrivedSaveColor),
                                lastMovement.getEndSquare());
                    }
                    if (turnFinished) {
                        turnFinished = false;
                        SixSidedDie = lastTurn.getSixSidedDie();
                        currentPlayer = lastTurn.getPlayerColor();
                    }
                    lastTurn.removeLastMovement();
                    return;
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public Turn getLastTurn() {
        if (playerTurnHistory.size() != 0)
            return playerTurnHistory.get(playerTurnHistory.size()-1);
        else
            return null;
    }

    /**
     *
     * @param i
     * @return
     */
    public Movement getNextMovement(int i) {
        Movement nextMovement = null;
        Turn currentTurn = null;
        int j = 0;

        for (Turn turn : playerTurnHistory) {
            currentTurn = turn;
            for (Movement movement : turn.getMovementList()) {
                j++;
                if(j == i) {
                    nextMovement = movement;
                    break;
                }
            }
        }

        if (nextMovement != null && currentTurn != null) {
            board.movePiece(nextMovement.getStartSquare(), nextMovement.getEndSquare());
        }

        return nextMovement;
    }

    /**
     *
     * @param i
     * @return
     */
    public Movement getPreviousMovement(int i) {
        Movement previousMovement = null;
        Turn currentTurn = null;
        int j = 0;

        for (Turn turn : playerTurnHistory) {
            currentTurn = turn;
            for (Movement movement : turn.getMovementList()) {
                j++;
                if(j == i) {
                    previousMovement = movement;
                    break;
                }
            }
        }

        if (previousMovement != null && currentTurn != null) {
            SquareColor squareArrivedSaveColor;
            if (previousMovement.getEndSquare().getCheckerColor() == SquareColor.WHITE)
                squareArrivedSaveColor = SquareColor.BLACK;
            else
                squareArrivedSaveColor = SquareColor.WHITE;

            board.movePiece(previousMovement.getEndSquare(), previousMovement.getStartSquare());

            if(previousMovement.isHitMove()) {
                board.movePiece(board.getBarSquare(squareArrivedSaveColor),
                        previousMovement.getEndSquare());
            }
        }

        return previousMovement;
    }

    public int getNumberOfStoredMoves() {
        int i = 0;
        for (Turn turn : playerTurnHistory) {
            for (Movement movement : turn.getMovementList()) {
                i++;
            }
        }
        return i;
    }
    /* SERIALIZATION */

    /**
     *
     * @param session
     */
    public void save(Element session) {
        Element game = new Element("game");
        session.addContent(game);

        Element doublingCubeXML = new Element("doublingCube");
        doublingCubeXML.setText(String.valueOf(doublingCube.getValue()));
        game.addContent(doublingCubeXML);

        Element currentPlayerXML = new Element("currentPlayer");
        currentPlayerXML.setText(String.valueOf(currentPlayer));
        game.addContent(currentPlayerXML);

        Element gameIdXML = new Element("gameId");
        gameIdXML.setText(String.valueOf(gameId));
        game.addContent(gameIdXML);
        
        Element diffIdXML = new Element("gameDifficulty");
        diffIdXML.setText(String.valueOf(this.gameDifficulty));
        game.addContent(diffIdXML);

        Element diceUsedXML = new Element("diceUsed");
        diceUsedXML.setText(String.valueOf(diceUsed));
        game.addContent(diceUsedXML);

        Element firstPlayerXML = new Element("firstPlayer");
        firstPlayerXML.setText(String.valueOf(firstPlayer));
        game.addContent(firstPlayerXML);

        Element SixSidedDieXML = new Element("SixSidedDie");
        game.addContent(SixSidedDieXML);

        for(int i = 0; i < SixSidedDie.size(); i++) {
            SixSidedDie.get(i).save(SixSidedDieXML);
        }

        board.save(game);

        Element playerTurnHistoryXML = new Element("playerTurnHistory");
        game.addContent(playerTurnHistoryXML);

        for(int i = 0; i < playerTurnHistory.size(); i++) {
            playerTurnHistory.get(i).save(playerTurnHistoryXML);
        }
    }

    /**
     *
     * @param game
     */
    public void load(Element game) {
        doublingCube = new DoublingCube(Integer.valueOf(game.getChildText("doublingCube")));

        switch(game.getChildText("currentPlayer")) {
            case "BLACK": currentPlayer = SquareColor.BLACK; break;
            case "WHITE": currentPlayer = SquareColor.WHITE; break;
            case "EMPTY": currentPlayer = SquareColor.EMPTY;
        }

        gameId = Integer.valueOf(game.getChildText("gameId"));
        gameDifficulty = GameDifficulty.valueOf(game.getChildText("gameDifficulty"));
        diceUsed = Integer.valueOf(game.getChildText("diceUsed"));

        switch(game.getChildText("firstPlayer")) {
            case "BLACK": firstPlayer = SquareColor.BLACK; break;
            case "WHITE": firstPlayer = SquareColor.WHITE; break;
            case "EMPTY": firstPlayer = SquareColor.EMPTY;
        }

        List<Element> SixSidedDieList = game.getChild("SixSidedDie").getChildren("sixSidedDie");
        Iterator<Element> it = SixSidedDieList.iterator();

        while(it.hasNext()) {
            SixSidedDie tmpDice = new SixSidedDie();
            tmpDice.load(it.next());
            SixSidedDie.add(tmpDice);
        }

        board = new Board(this);
        board.load(game);

        List<Element> playerTurnHistoryList = game.getChild("playerTurnHistory").getChildren("turn");
        Iterator<Element> i = playerTurnHistoryList.iterator();

        while(i.hasNext()) {
            Turn tmpTurn = new Turn(currentPlayer, SixSidedDie);
            tmpTurn.load(i.next(), this);
            playerTurnHistory.add(tmpTurn);
        }
    }
    public void nextTurn() {
        if (board.isAllPiecesMarked(currentPlayer)) {
            endGame();
            return; // Stop further execution after the game ends
        }

        if (skipNextTurn) {
            System.out.println("Skipping " + currentPlayer + "'s turn due to a special condition.");
            skipNextTurn = false; // Reset the skip flag after skipping once
            return;
        }

        // Handle AI turn logic if the current player is AI
        Player currentPlayerObj = gameParameters.getPlayer(currentPlayer);
        if (currentPlayerObj.isAI()) {
            System.out.println(currentPlayerObj.getUsername() + " (AI) is playing...");
            currentPlayerObj.makeMove(this);
        }

        // Change to the next player unless a special rule prevents it
        if (!surpriseStationHit || !surpriseStationEnabled) {
            currentPlayer = (currentPlayer == SquareColor.WHITE) ? SquareColor.BLACK : SquareColor.WHITE;
        }

        // Reset turn states
        SixSidedDie.clear();
        turnFinished = false;
        surpriseStationHit = false; // Reset surprise station flag
        questionRequiredAtTurnStart = gameDifficulty == GameDifficulty.MEDIUM || gameDifficulty == GameDifficulty.HARD;

        System.out.println("Next turn! Now it's " + gameParameters.getPlayer(currentPlayer).getUsername() + "'s turn.");
    }
    
    public void rollDi() {
        // Step 1: Clear the existing dice list to reset for the current turn
        SixSidedDie = new ArrayList<SixSidedDie>();

        // Step 2: Dynamically generate new dice based on the game's current difficulty level
        // - EASY: 2 regular dice
        // - MEDIUM: 2 regular dice + 1 question die
        // - HARD: 2 enhanced dice + 1 question die
        SixSidedDie.addAll(gameDifficulty.generateDice(currentPlayer));

        // Step 3: Mark the current turn as active (not yet finished)
        turnFinished = false;

        // Step 4: Start the player's turn and record the dice rolled
        beginTurn();
    }

    /* GETTERS AND SETTERS */

    public GameParameters getGameParameters() {
        return gameParameters;
    }
    
    public GameDifficulty getGameDiff() {
    	return this.gameParameters.getDifficulty();
    }

    public void setGameParameters(GameParameters gameParameters) {
        this.gameParameters = gameParameters;
    }

    public DoublingCube getDoublingCube() {
        return doublingCube;
    }

    public void setDoublingCube(DoublingCube doublingCube) {
        this.doublingCube = doublingCube;
    }

    public ArrayList<SixSidedDie> getSixSidedDie() {
        return SixSidedDie;
    }

    public void setSixSidedDie(ArrayList<SixSidedDie> SixSidedDie) {
        this.SixSidedDie = SixSidedDie;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public SquareColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(SquareColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Turn> getPlayerTurnHistory() {
        return playerTurnHistory;
    }

    public void setPlayerTurnHistory(ArrayList<Turn> playerTurnHistory) {
        this.playerTurnHistory = playerTurnHistory;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }
    @Override
    protected void initializeGame() {
        System.out.println("Initializing Backgammon game...");
        this.board = new Board();
        this.doublingCube = new DoublingCube();
        this.SixSidedDie = new ArrayList<>();
        this.playerTurnHistory = new ArrayList<>();
        this.currentPlayer = SquareColor.WHITE; // Example starting player
        this.firstPlayer = currentPlayer;
        this.gameFinished = false;
        this.turnFinished = true;
        this.skipNextTurn = false;
        this.surpriseStationEnabled = false;

        startGameTime(); // Record the game start time
        System.out.println("Game initialized. Starting with player: " + currentPlayer);
    }

    @Override
    protected boolean isGameOver() {
        return gameFinished || board.isAllPiecesMarked(currentPlayer);
    }

    @Override
    protected void playTurn() {
        System.out.println("Player " + currentPlayer + "'s turn.");
        
        // Roll dice for the current turn
        rollDice();

        // Perform the player's turn
        try {
            if (hasPossibleMove()) {
                // Example: Automatically play a random move
                randomMove();
            } else {
                System.out.println("No moves available for " + currentPlayer);
            }
        } catch (Exception e) {
            System.out.println("Error during the turn: " + e.getMessage());
        }

        // Check if a surprise station is enabled and trigger it if applicable
        if (surpriseStationEnabled && !surpriseStationHit) {
            System.out.println("Surprise station triggered!");
            surpriseStationHit = true; // Prevent triggering again in the same turn
        }

        // End the turn
        changeTurn();
    }

  
    public boolean isTurnFinished() {
        return turnFinished;
    }

    public SquareColor getFirstPlayer() {
        return firstPlayer;
    }
    public boolean shouldSkipNextTurn() {
        return skipNextTurn;
    }

    // Setter for skipNextTurn
    public void setSkipNextTurn(boolean skipNextTurn) {
        this.skipNextTurn = skipNextTurn;
    }
    public boolean isSurpriseStationEnabled() {
        return surpriseStationEnabled;
    }

    // Setter for surpriseStationEnabled
    public void setSurpriseStationEnabled(boolean surpriseStationEnabled) {
        this.surpriseStationEnabled = surpriseStationEnabled;
    }

    // Getter for surpriseStationHit
    public boolean isSurpriseStationHit() {
        return surpriseStationHit;
    }

    // Setter for surpriseStationHit
    public void setSurpriseStationHit(boolean surpriseStationHit) {
        this.surpriseStationHit = surpriseStationHit;
    }
 // Getter for the new field
    public boolean isQuestionRequiredAtTurnStart() {
        return questionRequiredAtTurnStart;
    }

    // Setter for the new field (used when the question is answered or skipped)
    public void setQuestionRequiredAtTurnStart(boolean required) {
        this.questionRequiredAtTurnStart = required;
    }
}
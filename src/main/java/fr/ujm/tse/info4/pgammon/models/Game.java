// 
//
//  @ Project : Project Gammon
//  @ File : Game.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

import fr.ujm.tse.info4.pgammon.exception.TurnNotPlayableException;

public class Game {
    private GameParameters gameParameters;
    private DoublingCube doublingCube;
    private ArrayList<SixSidedDice> sixSidedDice;
    private Board board;
    private PlayerColor firstPlayer;
    private PlayerColor currentPlayer;
    private ArrayList<Turn> playerTurnHistory;
    private int gameId;
    private boolean gameFinished;
    private boolean turnFinished;
    private int diceUsed;

    /**
     *
     * @param p
     */
    public Game(GameParameters p) {
        gameParameters = p;
        board = new Board();
        doublingCube = new DoublingCube();

        playerTurnHistory = new ArrayList<Turn>();

        sixSidedDice = new ArrayList<SixSidedDice>();

        //these variables track the game state
        turnFinished = true;
        gameFinished = false;
    }

    /**
     *
     * @param gameId
     * @param p
     */
    public Game(int gameId, GameParameters p) {
        gameParameters = p;
        this.gameId = gameId;

        board = new Board();
        doublingCube = new DoublingCube();

        playerTurnHistory = new ArrayList<Turn>();
        sixSidedDice = new ArrayList<SixSidedDice>();

        //these variables track the game state
        turnFinished = true;
        gameFinished = false;
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
    public void startNewGame(PlayerColor player) {
        gameFinished = false;

        if(player == PlayerColor.WHITE)
            currentPlayer = PlayerColor.BLACK;
        else
            currentPlayer = PlayerColor.WHITE;

        firstPlayer = currentPlayer;
    }

    /**
     *
     */
    public void beginTurn() {
        playerTurnHistory.add(new Turn(currentPlayer, sixSidedDice));
    }

    /**
     *
     */
    public void changeTurn() {
        if (board.isAllPiecesMarked(currentPlayer))
            endGame();
        else {
            if (currentPlayer == PlayerColor.WHITE)
                currentPlayer = PlayerColor.BLACK;
            else
                currentPlayer = PlayerColor.WHITE;
        }

        sixSidedDice = new ArrayList<SixSidedDice>();
        turnFinished = true;
    }

    /**
     *
     */
    public void endGame() {
        gameFinished = true;
    }

    /**
     *
     */
    public void chooseFirstPlayerGameStart() {
        ArrayList<SixSidedDice> choiceDice = new ArrayList<SixSidedDice>();
        choiceDice.add(new SixSidedDice(currentPlayer));
        choiceDice.add(new SixSidedDice(currentPlayer));

        if(choiceDice.get(0).getValue() == choiceDice.get(1).getValue()) {
            chooseFirstPlayerGameStart();
        }
        else if(choiceDice.get(0).getValue() > choiceDice.get(1).getValue())
            currentPlayer = PlayerColor.WHITE;
        else
            currentPlayer = PlayerColor.BLACK;

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
            PlayerColor enemyPlayer = (currentPlayer == PlayerColor.WHITE) ?
                    PlayerColor.BLACK : PlayerColor.WHITE;

            int piecesOnBar = board.getBarSquare(enemyPlayer).getPieceCount();

            if (board.movePiece(startSquare, endSquare)) {
                sixSidedDice.get(diceUsed).use();

                getLastTurn().addMovement(new Movement(startSquare, endSquare,
                        (piecesOnBar < board.getBarSquare(enemyPlayer).getPieceCount())));

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
    public boolean canMarkThisPiece(Square pieceSquare, SixSidedDice dice) {
        Square victorySquare = board.getVictorySquare(currentPlayer);

        if (((board.distanceBetweenSquares(pieceSquare, victorySquare) == dice.getValue()
                && currentPlayer == PlayerColor.WHITE)
                || (board.distanceBetweenSquares(pieceSquare, victorySquare) == -dice.getValue()
                && currentPlayer == PlayerColor.BLACK))
                && !dice.isUsed()) {
            return true;
        }
        else if (((board.distanceBetweenSquares(pieceSquare, victorySquare) < dice.getValue()
                && currentPlayer == PlayerColor.WHITE && !board.isSquareBefore(pieceSquare))
                || (board.distanceBetweenSquares(pieceSquare, victorySquare) > -dice.getValue()
                && currentPlayer == PlayerColor.BLACK) && !board.isSquareBefore(pieceSquare))
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

        for (int i = 0; i < sixSidedDice.size(); i++) {
            if (((board.distanceBetweenSquares(pieceSquare, victorySquare) == sixSidedDice.get(i).getValue()
                    && currentPlayer == PlayerColor.WHITE)
                    || (board.distanceBetweenSquares(pieceSquare, victorySquare) == -sixSidedDice.get(i).getValue()
                    && currentPlayer == PlayerColor.BLACK))
                    && !sixSidedDice.get(i).isUsed()) {
                isDiceAvailable = true;
                currentDiceUsed = i;
            }
            else if (((board.distanceBetweenSquares(pieceSquare, victorySquare) > sixSidedDice.get(i).getValue()
                    && currentPlayer == PlayerColor.WHITE && !board.isSquareBefore(pieceSquare))
                    || (board.distanceBetweenSquares(pieceSquare, victorySquare) > -sixSidedDice.get(i).getValue()
                    && currentPlayer == PlayerColor.BLACK) && !board.isSquareBefore(pieceSquare))
                    && !sixSidedDice.get(i).isUsed()) {
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

        for (int i = 0; i < sixSidedDice.size(); i++) {
            if(canMarkThisPiece(startSquare, sixSidedDice.get(i)) &&
                    endSquare.isVictorySquare() &&
                    endSquare.getPieceColor() == startSquare.getPieceColor()) {
                isDiceAvailable = true;
                diceUsed = i;
            }
            else if (((board.distanceBetweenSquares(startSquare, endSquare) == sixSidedDice.get(i).getValue()
                    && currentPlayer == PlayerColor.WHITE)
                    || (board.distanceBetweenSquares(startSquare, endSquare) == -sixSidedDice.get(i).getValue()
                    && currentPlayer == PlayerColor.BLACK))
                    && !sixSidedDice.get(i).isUsed()) {
                isDiceAvailable = true;
                diceUsed = i;
            }
        }

        if (!isDiceAvailable)
            return false;

        if (board.isCorrectMoveDirection(startSquare, endSquare)) {
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
        for (SixSidedDice dice : sixSidedDice) {
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
        for (SixSidedDice dice : sixSidedDice) {
            if (!dice.isUsed())
                return false;
        }
        return true;
    }
    /**
     *
     */
    public void rollDice() {
        sixSidedDice = new ArrayList<SixSidedDice>();
        sixSidedDice.add(new SixSidedDice(currentPlayer));
        sixSidedDice.add(new SixSidedDice(currentPlayer));
        if (sixSidedDice.get(0).getValue() == sixSidedDice.get(1).getValue()) {
            sixSidedDice.add(new SixSidedDice(currentPlayer, sixSidedDice.get(0).getValue()));
            sixSidedDice.add(new SixSidedDice(currentPlayer, sixSidedDice.get(0).getValue()));
        }
        turnFinished = false;
        beginTurn();
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
        for (SixSidedDice dice : sixSidedDice) {
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
            if(pieceSquare.getPieceColor() == currentPlayer) {
                for (SixSidedDice tmpDice : sixSidedDice) {
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
    public boolean hasPossibleMoves() {
        for (Square pieceSquare : board.getAllSquares()) {
            if((!board.isPieceOnBar(currentPlayer) && pieceSquare.getPieceColor() == currentPlayer)
                    || pieceSquare.isBarSquare())
                for (SixSidedDice dice : sixSidedDice) {
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
    public void cancelLastMove() {
        Movement lastMovement;

        Turn lastTurn = getLastTurn();
        if (lastTurn != null)
            lastMovement = getLastTurn().getLastMovement();
        else
            lastMovement = null;

        if (lastMovement != null) {
            for (SixSidedDice dice : lastTurn.getSixSidedDice()) {
                if (dice.isUsed() && dice.getValue() == Math.abs(board.distanceBetweenSquares(
                        lastMovement.getEndSquare(), lastMovement.getStartSquare()))) {

                    PlayerColor squareArrivedSaveColor;
                    if (lastMovement.getEndSquare().getPieceColor() == PlayerColor.WHITE)
                        squareArrivedSaveColor = PlayerColor.BLACK;
                    else
                        squareArrivedSaveColor = PlayerColor.WHITE;

                    board.movePiece(lastMovement.getEndSquare(), lastMovement.getStartSquare());
                    dice.setUnused();
                    if(getLastTurn().getLastMovement().isSquareHit()) {
                        board.movePiece(board.getBarSquare(squareArrivedSaveColor),
                                lastMovement.getEndSquare());
                    }
                    if (turnFinished) {
                        turnFinished = false;
                        sixSidedDice = lastTurn.getSixSidedDice();
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
            PlayerColor squareArrivedSaveColor;
            if (previousMovement.getEndSquare().getPieceColor() == PlayerColor.WHITE)
                squareArrivedSaveColor = PlayerColor.BLACK;
            else
                squareArrivedSaveColor = PlayerColor.WHITE;

            board.movePiece(previousMovement.getEndSquare(), previousMovement.getStartSquare());

            if(previousMovement.isSquareHit()) {
                board.movePiece(board.getBarSquare(squareArrivedSaveColor),
                        previousMovement.getEndSquare());
            }
        }

        return previousMovement;
    }

    public int getNumberOfStoredMovements() {
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

        Element diceUsedXML = new Element("diceUsed");
        diceUsedXML.setText(String.valueOf(diceUsed));
        game.addContent(diceUsedXML);

        Element firstPlayerXML = new Element("firstPlayer");
        firstPlayerXML.setText(String.valueOf(firstPlayer));
        game.addContent(firstPlayerXML);

        Element sixSidedDiceXML = new Element("sixSidedDice");
        game.addContent(sixSidedDiceXML);

        for(int i = 0; i < sixSidedDice.size(); i++) {
            sixSidedDice.get(i).save(sixSidedDiceXML);
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
            case "BLACK": currentPlayer = PlayerColor.BLACK; break;
            case "WHITE": currentPlayer = PlayerColor.WHITE; break;
            case "EMPTY": currentPlayer = PlayerColor.EMPTY;
        }

        gameId = Integer.valueOf(game.getChildText("gameId"));
        diceUsed = Integer.valueOf(game.getChildText("diceUsed"));

        switch(game.getChildText("firstPlayer")) {
            case "BLACK": firstPlayer = PlayerColor.BLACK; break;
            case "WHITE": firstPlayer = PlayerColor.WHITE; break;
            case "EMPTY": firstPlayer = PlayerColor.EMPTY;
        }

        List<Element> sixSidedDiceList = game.getChild("sixSidedDice").getChildren("sixSidedDie");
        Iterator<Element> it = sixSidedDiceList.iterator();

        while(it.hasNext()) {
            SixSidedDice tmpDice = new SixSidedDice();
            tmpDice.load(it.next());
            sixSidedDice.add(tmpDice);
        }

        board = new Board(this);
        board.load(game);

        List<Element> playerTurnHistoryList = game.getChild("playerTurnHistory").getChildren("turn");
        Iterator<Element> i = playerTurnHistoryList.iterator();

        while(i.hasNext()) {
            Turn tmpTurn = new Turn();
            tmpTurn.load(i.next(), this);
            playerTurnHistory.add(tmpTurn);
        }
    }

    /* GETTERS AND SETTERS */

    public GameParameters getGameParameters() {
        return gameParameters;
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

    public ArrayList<SixSidedDice> getSixSidedDice() {
        return sixSidedDice;
    }

    public void setSixSidedDice(ArrayList<SixSidedDice> sixSidedDice) {
        this.sixSidedDice = sixSidedDice;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerColor currentPlayer) {
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

    public boolean isTurnFinished() {
        return turnFinished;
    }

    public PlayerColor getFirstPlayer() {
        return firstPlayer;
    }
}
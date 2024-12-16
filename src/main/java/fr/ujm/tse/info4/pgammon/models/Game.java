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
    private ArrayList<SixSidedDie> SixSidedDie;
    private Board board;
    private SquareColor firstPlayer;
    private SquareColor currentPlayer;
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

        SixSidedDie = new ArrayList<SixSidedDie>();

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
        SixSidedDie = new ArrayList<SixSidedDie>();

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
     *
     */
    public void changeTurn() {
        if (board.isAllPiecesMarked(currentPlayer))
            endGame();
        else {
            if (currentPlayer == SquareColor.WHITE)
                currentPlayer = SquareColor.BLACK;
            else
                currentPlayer = SquareColor.WHITE;
        }

        SixSidedDie = new ArrayList<SixSidedDie>();
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
        SixSidedDie.add(new SixSidedDie(currentPlayer));
        SixSidedDie.add(new SixSidedDie(currentPlayer));
        if (SixSidedDie.get(0).getValue() == SixSidedDie.get(1).getValue()) {
            SixSidedDie.add(new SixSidedDie(currentPlayer, SixSidedDie.get(0).getValue()));
            SixSidedDie.add(new SixSidedDie(currentPlayer, SixSidedDie.get(0).getValue()));
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
    public boolean hasPossibleMoves() {
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
    public void cancelLastMove() {
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

    public boolean isTurnFinished() {
        return turnFinished;
    }

    public SquareColor getFirstPlayer() {
        return firstPlayer;
    }
}
package Controller;

import java.util.ArrayList;

import game.Column;
import game.Game;
import game.Move;
import game.PossibleMove;
import ai.AI;
import ai.aoi.PossibleBoard;

/**
 * AI that calculates the value of the resulting board to 
 * decide which move is the best.
 */
public class Aoi implements AI{
	
	//these values are ones we manually decided for Aoi to evaluate the board
	//she always tries to aim for a high value board
	public static final int SINGLE_SAME_PIECE_VALUE = -10;
	public static final int SINGLE_DIFF_PIECE_VALUE = -100;
	public static final int PAIR_VALUE = 25;
	public static final int PIP_VALUE = 25;
	public static final int GREATER_THAN_PAIR_VALUE = 10;
	public static final int END_VALUE = 75;
	public static final int WOOD_VALUE = -50;
	
	private ArrayList<PossibleBoard> possibleBoards = new ArrayList<>();
	private int[] currentBoard;

	public void makeMove() {
		
		currentBoard = getBoardState();
		possibleBoards = new ArrayList<>();
		
		//for every possible move make a new board
		for (PossibleMove move : Move.possibleMoves) {
			int[] board = getNewBoardState(move);
			possibleBoards.add(new PossibleBoard(board, move));
		}
		
		PossibleBoard bestBoard = null;
		int bestBoardValue = Integer.MIN_VALUE;
		//from all the boards made, get the board with the best value
		for (PossibleBoard board: possibleBoards) {
			if (board.getValue() > bestBoardValue) {
				bestBoardValue = board.getValue();
				bestBoard = board;
			}
		}
		Move.executeMove(Game.gameBoard,bestBoard.getMove(),true);
	}
	
	/**
	 * Turns the board state into a set of values for each column.
	 * @return an array which represents the value of the board.
	 */
	public int[] getBoardState() {
		int[] boardState = new int[Game.gameBoard.getAll().length + 2];
		for (Column c: Game.gameBoard.getAll()) {
			
			boardState[c.getNumber()] = c.getPieces().size()*c.getNumber()*c.getColor()*PIP_VALUE;
			
			if (c.getPieces().size() == 1 && c.getColor() == Game.gameBoard.getTurn()) {
				boardState[c.getNumber()] += SINGLE_SAME_PIECE_VALUE; 
			}
			if (c.getPieces().size() == 1 && c.getColor() != Game.gameBoard.getTurn()) {
				boardState[c.getNumber()] += SINGLE_DIFF_PIECE_VALUE;
			}
			if (c.getPieces().size() == 2 && c.getColor() == Game.gameBoard.getTurn()) {
				boardState[c.getNumber()] += PAIR_VALUE;
			}
			if (c.getPieces().size() > 2 && c.getColor() == Game.gameBoard.getTurn()) {
				boardState[c.getNumber()] += GREATER_THAN_PAIR_VALUE;
			}
			if (c.getNumber() == 0 || c.getNumber() == 25) {
				boardState[c.getNumber()] += END_VALUE;
			}
		}
		for (int i = 0; i <=1; i++){
			if (Game.gameBoard.woodColumns[i].hasPieces() && Game.gameBoard.woodColumns[i].getColor() == Game.gameBoard.getTurn()){
				boardState[Game.gameBoard.getAll().length + i] += WOOD_VALUE*Game.gameBoard.woodColumns[i].getPieces().size();
			}
		}
		return boardState;
	}

	/**
	 * Evaluates the impact of a move on the value of the board.
	 * @param move the move to be evaluated.
	 * @return an array representing the new board state.
	 */
	private int[] getNewBoardState(PossibleMove move) {
		int[] newBoard = currentBoard.clone();
		
		int fromIndex = move.getFrom();
		int toIndex = move.getTo();
		
		Game.gameBoard.setSelected(Game.gameBoard.find(fromIndex));
		
		if (Game.gameBoard.getSelected().isWoodColumn()){
			newBoard[fromIndex] -= WOOD_VALUE;
		} else {
			//change FROM column value
			if (Game.gameBoard.getSelected().getPieces().size() == 1){
				//lone piece moving somewhere
				newBoard[fromIndex] -= SINGLE_SAME_PIECE_VALUE;				
			}
			if (Game.gameBoard.getSelected().getPieces().size() == 2){
				newBoard[fromIndex] -= PAIR_VALUE;
				newBoard[fromIndex] += SINGLE_SAME_PIECE_VALUE;
			}
			if (Game.gameBoard.getSelected().getPieces().size() == 3){
				newBoard[fromIndex] -= GREATER_THAN_PAIR_VALUE;
				newBoard[fromIndex] += PAIR_VALUE;
			}
		}		
		
		//change TO column value
		if (Game.gameBoard.find(toIndex).getPieces().size() == 0){
			//lone piece moving somewhere
			newBoard[toIndex] += SINGLE_SAME_PIECE_VALUE;				
		}
		if (Game.gameBoard.find(toIndex).getPieces().size() == 1){
			if (Game.gameBoard.find(toIndex).getColor() != Game.gameBoard.getTurn()){
				newBoard[toIndex] -= SINGLE_DIFF_PIECE_VALUE;
			} else {
				newBoard[toIndex] += PAIR_VALUE;
				newBoard[toIndex] -= SINGLE_SAME_PIECE_VALUE;				
			}
		}
		if (Game.gameBoard.find(toIndex).getPieces().size() == 2){
			newBoard[toIndex] += GREATER_THAN_PAIR_VALUE;
			newBoard[toIndex] -= PAIR_VALUE;
		}			
		
		Game.gameBoard.setSelected(null);
		return newBoard;
	}
}

package ai.miki;

import game.Board;
import game.Column;
import game.Game;
import game.Move;
import game.PossibleMove;

import java.util.ArrayList;

import ai.aoi.Aoi;

/**
 * A future representation of the board used by Miki.
 *
 */
public class FutureBoard extends Board {
	
	/**
	 * @see Aoi
	 * Slightly modified version of Aois values.
	 */
	public static final int SINGLE_SAME_PIECE_VALUE = -25;
	public static final int SINGLE_DIFF_PIECE_VALUE = 100;
	public static final int PAIR_VALUE = 50;
	public static final int GREATER_THAN_PAIR_VALUE = -100;
	public static final int END_VALUE = 75;
	public static final int WOOD_VALUE = -50;
	public static final int OTHER_WOOD_VALUE = 300;
	
	ArrayList<FutureBoard> futureBoards;
	private int playerColor;
	private PossibleMove boardMove;
	
	private int boardValue;
	
	public static final int MAX_DEPTH = 2;
	public static int NUMBEROFBOARDS;
	
	public FutureBoard(PossibleMove boardMove, Board board, int depth, int playerColor) {
		super(board.getTurn());
		this.init();
		this.boardMove = boardMove;
		this.addPieces(board);
		futureBoards = new ArrayList<>();
		Move.setDice(this, board.getDice());
		Move.executeMove(this, boardMove, false);
		NUMBEROFBOARDS++;
		this.playerColor = playerColor;

		calculateValue();
		makeFutureBoards(depth);

	}

	/**
	 * An improved version of Aoi's evaluation.
	 * Called on construction to set the value of the board.
	 */
	public void calculateValue() {
		boardValue = 0;
		for (Column c: this.getAll()) {
				
			boardValue += c.getPieces().size()*c.getNumber()*c.getColor()*20;
			
			if (c.getPieces().size() == 1 && c.getColor() == this.getTurn()) {
				boardValue += SINGLE_SAME_PIECE_VALUE; 
			}
			if (c.getPieces().size() == 1 && c.getColor() != this.getTurn()) {
				boardValue += SINGLE_DIFF_PIECE_VALUE;
			}
			if (c.getPieces().size() == 2 && c.getColor() == this.getTurn()) {
				boardValue += PAIR_VALUE;
			}
			if (c.getPieces().size() > 2 && c.getColor() == this.getTurn()) {
				boardValue += GREATER_THAN_PAIR_VALUE;
			}
			if (c.getNumber() == 0 || c.getNumber() == 25) {
				boardValue += END_VALUE;
			}
		}
		for (int i = 0; i <=1; i++){
			if (this.woodColumns[i].hasPieces() && this.woodColumns[i].getColor() == this.getTurn()){
				boardValue += WOOD_VALUE*this.woodColumns[i].getPieces().size();
			}
			if (this.woodColumns[i].hasPieces() && this.woodColumns[i].getColor() != this.getTurn()){
				boardValue += OTHER_WOOD_VALUE*this.woodColumns[i].getPieces().size();
			}
		}
	}
	
	/**
	 * Creates children if the depth is less than the specified maximum depth.
	 * @param depth the current depth.
	 */
	private void makeFutureBoards(int depth) {
		ArrayList<PossibleMove> moves = new ArrayList<>();
		if (depth < MAX_DEPTH) {
			//For each dice roll, make a possible future board.
			for (int i = 1; i <= 6; i++){
				for (int j = 1; j <= 6; j++){
					int[] tempDice = {i,j};
					Move.setDice(this, tempDice);
					Move.setPossibleMoves(this);
					for (PossibleMove move : Move.possibleMoves){
						moves.add(move.clone());
					}
					for (PossibleMove move : moves){
						FutureBoard possibleBoard = new FutureBoard(this.boardMove,this, depth + 1, playerColor);
						this.futureBoards.add(possibleBoard);
						Move.executeMove(possibleBoard, move, false);
						Game.gameWindow.repaint();

					}
				}
			}
		}
	}
	
	public PossibleMove getMove() {
		return boardMove;
	}

	/**
	 * Calculates the value of the board based on itself and its children.
	 * @return the value of the branch.
	 */
	public double evaluate() {
		double childValue = 0;
		if(!futureBoards.isEmpty()){
			for (FutureBoard board : futureBoards){
				childValue += board.evaluate();
			}
			childValue = (childValue/(1.0*futureBoards.size()));
			boardValue += childValue;
		}
		return boardValue;
	}
}

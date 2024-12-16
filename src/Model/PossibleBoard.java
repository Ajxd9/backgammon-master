package Model;

import game.PossibleMove;

/**
 * A future board state used by Aoi to evalute.
 *
 */
public class PossibleBoard {

	private int[] boardState;
	private PossibleMove move;
	
	public int[] getBoardState() {
		return boardState;
	}
	
	public PossibleMove getMove(){
		return move;
	}
	
	public PossibleBoard(int[] board, PossibleMove move) {
		this.boardState = board;
		this.move = move;
	}
	
	public int getValue() {
		int value = 0;
		for (int i : boardState){
			value += i;
		}
		return value;
	}
	
	

}

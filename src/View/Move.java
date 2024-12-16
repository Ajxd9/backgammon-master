package View;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class to control game logic, such as: 
 * Setting possible moves
 * Rolling dice
 * Passing the turn
 *
 */
public class Move {

	public static String message = " ";
	private static Random random = new Random();
	public static ArrayList<PossibleMove> possibleMoves = new ArrayList<>();

	public static void setDice(Board board, int[] dices){
		board.setDice(dices);
		if (dices[0] == dices[1]){
			board.setDoubles(dices.clone());
		} else {
			int[] doubles = {0,0};
			board.setDoubles(doubles);
		}
		
		Move.setPossibleMoves(board);
	}

	public static void rollDice(Board board) {

		int[] dice = {random.nextInt(5) + 1,random.nextInt(5) + 1 };
		int[] doubles = {0,0};
		if (dice[0] == dice[1]){
			doubles = dice.clone();
		}
		doubles[0] = 0;
		doubles[1] = 0;

		dice[0] = random.nextInt(5)+ 1;
		dice[1] = random.nextInt(5)+ 1;
		message = " " + dice[0] + "-" + dice[1] + ":";

		if (dice[0] == dice[1]){
			doubles[0] = dice[0];
			doubles[1] = dice[1];
		}
		
		board.setDice(dice);
		board.setDoubles(doubles);
		
		Move.setPossibleMoves(board);
	}
	
	/**
	 * Checks if a column in a board has valid moves.
	 * @param board the board to be checked.
	 * @param c the column in the board.
	 * @return the existance of valid moves.
	 */
	public static boolean hasValidMoves(Board board, Column c){
		if (c.getColor() == board.getTurn()){
			Column temp = null;
			if (board.getSelected() != null){
				temp = board.getSelected();
			}
			board.setSelected(c);
			for (int move : board.getDice()){
				int x = c.getNumber() + move*c.getColor();
				if (x >= 0 && x < board.getAll().length){
					if (board.find(c.getNumber() + move*c.getColor()).isValidMove()){
						return true;
					}
				}
				
				if( board.canBearOff()){
					if (c.getColor() == Column.BLACK){
						if (c.getNumber() < move){
							return true;
						}
					} else if (c.getColor() == Column.WHITE){
						if (c.getNumber() > 25 - move){
							return true;
						}
					}
				}
				
			}
			board.setSelected(null);	
			if (temp != null){
				board.setSelected(temp);
			}
		}
		return false;
	}


	/**
	 * Finds all possible moves in a board.
	 * @param board the board to check.
	 * @return a list of all possible moves.
	 */
	public static ArrayList<PossibleMove> setPossibleMoves(Board board){
		possibleMoves.clear();
		for (Column c : board.woodColumns){
			if (hasValidMoves(board, c)){
				for (int move : board.getDice()){
					int x = c.getNumber() + move*c.getColor();
					if (x >= 0 && x < board.getAll().length){
						if (board.find(c.getNumber() + move*c.getColor()).isValidMove()){
							possibleMoves.add(new PossibleMove(x,c.getNumber(),move));
						}						
					}
				}
			}
		}
		//If there are moves from the bar, they must be played first.
		//If there is a piece on the bar, it must be moved first.
		if (possibleMoves.isEmpty() && board.getWood(board.getTurn()).isEmpty() ) {
			for (Column c: board.getAll()){
				if (hasValidMoves(board, c)){
					for (int move : board.getDice()){
						int x = c.getNumber() + move*c.getColor();
						if (x >= 0 && x < board.getAll().length){
							if (board.find(c.getNumber() + move*c.getColor()).isValidMove() ){
								possibleMoves.add(new PossibleMove(x,c.getNumber(),move));
							}
						}

						if (c.getColor() == Column.BLACK){
							if (x < 0 && board.canBearOff() && c.hasToBearOff() ){
								if (c.getNumber() != 0){
									possibleMoves.add(new PossibleMove(0,c.getNumber(),move));									
								}
							}
						}
						
						if (c.getColor() == Column.WHITE) {
							if (x > 25 && board.canBearOff() && c.hasToBearOff() ){
								if (c.getNumber() != 25) {
									possibleMoves.add(new PossibleMove(25, c.getNumber(), move));									
								}
							}								
						}

					}
				}
			}			
		}

		return sanitizeMoves(board);
	}
	
	/**
	 * Removes moves based on fringe cases that would be invalid
	 * under the full rule set.
	 * @param board
	 * @return
	 */
	private static ArrayList<PossibleMove> sanitizeMoves(Board board) {
		if (possibleMoves.isEmpty()){
			return possibleMoves;
		}

		ArrayList<PossibleMove> tempMoves = new ArrayList<>();
		for (PossibleMove move : Move.possibleMoves){
			tempMoves.add(move.clone());
		}
		int[] tempDice = board.getDice().clone();
		
		boolean allMovesSame = true;
		int commonMove = possibleMoves.get(0).getDiceUsed();
		for (PossibleMove move : possibleMoves){
			if (move.getDiceUsed() != commonMove){
				allMovesSame = false;
			}
		}
		ArrayList<PossibleMove> newMoves = new ArrayList<>();
		
		if (!allMovesSame){
			return possibleMoves;
		} else {
			for (PossibleMove move : Move.possibleMoves){
				int futureIndex = move.getFrom() + (board.getDice()[0] + board.getDice()[1])*board.getTurn() ;
				if (futureIndex > 0 && futureIndex < board.getAll().length){
					if (board.getAll()[futureIndex].getColor() != board.getTurn()*-1){
						newMoves.add(move);
					}
				}
			}
		}
		
		if (newMoves.isEmpty()){
			return Move.possibleMoves;
		} else {
			return newMoves;
		}
		
	}

	public static void executeMove(Board board, PossibleMove move, boolean shareToNetwork) {
		// Assume a valid move is passed here.
		Column from = board.findFrom(move);
		Column to = board.findTo(move);
		if (to.getPieces().size() == 1){
			if (from.getColor() == Column.WHITE && to.getColor() == Column.BLACK){
				board.find(Board.WOOD_BLACK).addPiece(to.RemovePiece());
			} else if (from.getColor() == Column.BLACK && to.getColor() == Column.WHITE) {
				board.find(Board.WOOD_WHITE).addPiece(to.RemovePiece());
			}
		}

		to.addPiece(from.RemovePiece());
		
		if (board.equals(Game.gameBoard)){
			Move.consumeMove(board, move,shareToNetwork);
		}
	}

	/**
	 * Removes a die from the board.
	 * @param board the board.
	 * @param move the move to get the die from.
	 * @param shareToNetwork Whether or not to share this move to the network.
	 * shareToNetwork should be false when executing moves received from the network.
	 */
	private static void consumeMove(Board board, PossibleMove move, boolean shareToNetwork) {
		boolean gotDice = false;
		for (int i = 0; i < board.getDice().length; i++){
			if (board.getDoubles()[i] == move.getDiceUsed()){
				board.setDiceFromDouble(i);
				gotDice = true;
				break;				
			}
		}
		if (!gotDice){
			for (int i = 0; i < board.getDice().length; i++){
				if (board.getDice()[i] == move.getDiceUsed()){
					board.consumeDice(i);
					break;
				}
			}			
		}
		if (shareToNetwork){
			message = message + "(" + move.getFrom() + "|" + move.getTo() + "),";
		}
		Move.setPossibleMoves(board);
		
		if (possibleMoves.isEmpty() || (board.getDice()[0] == 0 && board.getDice()[1] == 0 )){
			if (board.equals(Game.gameBoard)){
				Game.changeTurn();				
			}
		}
	}

	public static PossibleMove find(int from, int to) {
		for (PossibleMove move : possibleMoves){
			if (move.getFrom() == from && move.getTo() == to){
				return move;
			}
		}
		return null;
	}

	public static void passTurn(Board board) {
		int[] zeroDice = {0,0};
		board.setDice(zeroDice);
		board.setDoubles(zeroDice);
		Game.changeTurn();
	}
}



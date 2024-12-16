package Controller;

import java.util.ArrayList;
import java.util.Random;

import ai.AI;
import ai.random.RandomAI;
import game.Column;
import game.Game;
import game.Move;
import game.PossibleMove;

/**
 * Tree based AI that calculates which move to make
 * based on the value of future boards that arise
 * from making that move.
 *
 */
public class Miki implements AI {
	

	ArrayList<FutureBoard> futureBoards;
	
	public Miki(){

	}

	public void makeMove() {
		
		futureBoards = new ArrayList<>();
		Move.setPossibleMoves(Game.gameBoard);
		ArrayList<PossibleMove> boardMoves = new ArrayList<>();
		
		for (PossibleMove move : Move.possibleMoves){
			boardMoves.add(move.clone());
		}
	
		for (PossibleMove move : boardMoves){
			FutureBoard newBoard = new FutureBoard(move, Game.gameBoard, 1, Game.gameBoard.getTurn());
			futureBoards.add(newBoard);
		}

		PossibleMove bestMove = evaluteMove();
		
		Move.setPossibleMoves(Game.gameBoard);
		Move.executeMove(Game.gameBoard,bestMove,true);
		
		FutureBoard.NUMBEROFBOARDS = 0;
	}

	/**
	 * Searches for the best move using each of the future boards.
	 * @return the best PossibleMove.
	 */
	private PossibleMove evaluteMove() {
		
		PossibleMove bestMove = null;
		double bestValue = Integer.MIN_VALUE;
		for (FutureBoard board : this.futureBoards){
			if (board.evaluate() > bestValue){
				bestValue = board.evaluate();
				bestMove = board.getMove();
			}
		}
		return bestMove;
	}
}


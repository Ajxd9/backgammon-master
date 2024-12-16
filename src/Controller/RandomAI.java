package Controller;

import game.Column;
import game.Game;
import game.Move;
import game.PossibleMove;

import java.util.Random;

import ai.AI;

/**
 * A random AI that makes a random move each time.
 *
 */
public class RandomAI implements AI {

	Random generator;
	
	public RandomAI(){
		generator = new Random();
	}
	
	public void makeMove() {
		int chosenMove = generator.nextInt(Move.possibleMoves.size());
		PossibleMove move = Move.possibleMoves.get(chosenMove);
		Move.executeMove(Game.gameBoard,move,true);			
	}

}

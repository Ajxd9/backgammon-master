package ai.homura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Serializable class that is read and written by Homura to compile
 * a list of the best moves in a given board state.
 * @see Homura
 *
 */
public class Timeline implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<TimelineMove> timelineMoves;
	
	public Timeline(){
		timelineMoves = new ArrayList<TimelineMove>();
	}
	
	/**
	 * Adds a set of winning moves to the timeline.
	 * @param gameMoves the list of game moves played in the game.
	 */
	public void pushWinData(ArrayList<TimelineMove> gameMoves) {
		for (TimelineMove move : gameMoves){
			boolean moveExists = false;
			for (TimelineMove savedMove : timelineMoves){
				if (Arrays.equals(move.getBoardState(), savedMove.getBoardState()) && move.getFrom() == savedMove.getFrom() && move.getTo() == savedMove.getTo()){
					moveExists = true;
					savedMove.addWin();
				}
			}
			if (!moveExists){
				move.setWins(1);
				timelineMoves.add(move);
			}
		}
	}

	/**
	 * Adds a set of losing moves to the timeline.
	 * @param gameMoves the list of game moves played in the game.
	 */
	public void pushLoseData(ArrayList<TimelineMove> gameMoves) {
		for (TimelineMove move : gameMoves){
			boolean moveExists = false;
			for (TimelineMove savedMove : timelineMoves){
				if (Arrays.equals(move.getBoardState(), savedMove.getBoardState()) && move.getFrom() == savedMove.getFrom() && move.getTo() == savedMove.getTo()){
					moveExists = true;
					savedMove.addLoss();
				}
			}
			if (!moveExists){
				move.setLoses(1);
				timelineMoves.add(move);
			}
		}		
	}
	
}

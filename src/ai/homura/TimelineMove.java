package ai.homura;

import game.Column;
import game.Piece;

import java.io.Serializable;

/**
 * Serializable class that is contained in the Timeline file.
 * Contains the move that was played, plus the wins and losses of 
 * the particular move.
 *
 */
public class TimelineMove implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] boardState;
	private int from;
	private int to;
	private int wins;
	private int loses;
	private double value;
	
	public static int[] makeBoardState(Column[] columns) {
		int[] boardState = new int[columns.length];
		for (int i=0; i<columns.length; i++) {
			if (columns[i].isEmpty()) {
				boardState[i] = 0;
			} else if (columns[i].getColor() == Column.BLACK) {
				boardState[i] = columns[i].getPieces().size()*Column.BLACK;
			} else if (columns[i].getColor() == Column.WHITE) {
				boardState[i] = columns[i].getPieces().size()*Column.WHITE;
			}
		}
		return boardState;	
	}
	
	public TimelineMove(Column[] boardState, int from, int to) {
		this.boardState = makeBoardState(boardState);
		this.from = from;
		this.to = to;
		this.wins = 0;
		this.loses = 0;
		this.value = 0.0;
	}
	
	public void addWin(){
		this.wins++;
		this.calculateValue();
	}
	
	public void addLoss(){
		this.loses++;
		this.calculateValue();
	}
	
	private void calculateValue(){
		this.value = 100.0*(this.wins/(1.0* this.loses)) - 50.0;
	}
	
	public double getValue(){
		calculateValue();
		return this.value;
	}

	public int[] getBoardState() {
		return boardState;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLoses() {
		return loses;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}
	
	
}

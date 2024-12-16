package View;

import game.Column;
import game.Game;
import game.Move;
import game.PossibleMove;

public class Board {

	/**
	 * Sets a column in this board highlighted based on a column number.
	 * @param from the column number.
	 */
	public void setHighlighted(int from) {
		for (Column c : columns){
			for (PossibleMove move : Move.possibleMoves){
				if (c.getNumber() == move.getTo() && move.getFrom() == from){
					c.isHighlighted = true;
				}
			}
		}
	}
	
	/**
	 * Checks whether this board has pieces that can move off the bar.
	 * @return whether or not wood moves are possible.
	 */
	public boolean hasWoodMoves() {
		for (Column c: woodColumns) {
			if (Move.hasValidMoves(this, c)) {
				return true;
			}
		}
		return false;
	}

	public void unSelect() {
		for (Column c : columns){
			c.isSelected = false;
			c.isHighlighted = false;
		}
		for (Column c : woodColumns){
			c.isSelected = false;
			c.isHighlighted = false;
		}
		selectedColumn = null;
		Game.gameWindow.repaint();
}

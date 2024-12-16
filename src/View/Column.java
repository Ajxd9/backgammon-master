package View;

import gui.game.ColumnPanel;
import java.util.ArrayList;
import Model.Board;
import Controller.BoardController;
import Model.Piece;

public class Column {

	


	public boolean RemovePiece(){
		if (this.pieces.size() > 0){
			boolean b = this.pieces.get(0).getColor();
			this.pieces.remove(this.pieces.get(0));
			return b;			
		}
		return false;
	}

	public int getNumber() {
		if (this.number == Board.WOOD_BLACK){
			return 25;
		} else if (this.number == Board.WOOD_WHITE){
			return 0;
		} else {
			return number;			
		}
	}

	public boolean hasPieces(){
		return !pieces.isEmpty();
	}

	/**
	 * Gets the state of the Column.
	 * @see Column#EMPTY
	 * @return One of the constants representing the state of the column.
	 */
	public int getColor(){
		if (this.pieces.isEmpty()){
			return EMPTY;
		} else {
			if (this.pieces.get(0).getColor() == Piece.BLACK){
				return BLACK;
			} else {
				return WHITE;
			}
		}
	}

	public boolean matchesColor(){
		return board.getSelected().getColor() == this.getColor();
	}
	
	public boolean isEmpty(){
		return (this.getColor() == Column.EMPTY);
	}
	
	public boolean isCapturable(){
		return (this.getPieces().size() == 1);
	}
	
	public boolean isForwardMove(){
		Column selected = board.getSelected();

		return (
				((this.getNumber() == 25 || this.getNumber() == 0) && board.canBearOff() || (this.getNumber() != 25 && this.getNumber() != 0)) && (					
					(selected.getColor() == WHITE && this.number > selected.number) ||
					(selected.getColor() == BLACK && this.number < selected.number) ||
					(selected.isWoodColumn())
				)
		);
	}
		
	public boolean isLegalMove(){
		for (int move : board.getDice()){
			if (move == this.getMoveNumber()){
				return true;
			}
		}
		return false;
	}
	
	boolean hasToBearOff() {
		if (this.getColor() == Column.BLACK){
			for (int i = 6; i > this.getNumber(); i--){
				if (board.find(i).hasPieces() && board.find(i).matchesColor()){
					return false;
				}
			}
		} else {
			for (int i = 19; i < this.getNumber(); i++){
				if (board.find(i).hasPieces() && board.find(i).matchesColor()){
					return false;
				}
			}			
		}
		return true;
	}
	
	public boolean isValidMove(){
		return ( this.isForwardMove() && !this.isWoodColumn() && this.isLegalMove() && (		
				this.isEmpty() || this.matchesColor() || (!this.matchesColor() && this.isCapturable())
				)
		);
	}
	
	public int getMoveNumber(){
		if (board.getSelected() == board.find(Board.WOOD_BLACK)){
			return 25 - this.getNumber();
		} else if (board.getSelected() == board.find(Board.WOOD_WHITE)){
			return this.getNumber();
		} else {
			return Math.abs(board.getSelected().getNumber() - this.getNumber());			
		}
	}
	
	/**
	 * Selects the column if valid.
	 */
	public void select(){
		Game.gameWindow.repaint();
		if (board.getSelected() != null){
			
			for (PossibleMove move : Move.possibleMoves){
				if (this.getNumber() == move.getTo() && board.getSelected().getNumber() == move.getFrom()){
					Move.executeMove(this.board,move, true);
					if (board.getSelected() != null){
						board.unSelect();
					}
					return;
				}
			}
			if (this.matchesColor() && ( Game.blackIsHuman && this.board.getTurn() == Board.BLACK || Game.whiteIsHuman && this.board.getTurn() == Board.WHITE ) ){
				board.unSelect();
				this.select();
			}
		} else {
			// If there is a move that originates from a column, it is selectable.
			for (PossibleMove move : Move.possibleMoves){
				if (this.getNumber() == move.getFrom() && 
						( Game.blackIsHuman && this.board.getTurn() == Board.BLACK || Game.whiteIsHuman && this.board.getTurn() == Board.WHITE )){
					board.setSelected(this);
					this.isSelected = true;
					board.setHighlighted(move.getFrom());
				}
			}			
		}

	}

	public boolean isWoodColumn() {
		for (Column c: board.woodColumns){
			if (this.equals(c)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isSelected(){
		return isSelected;
	}

	public void addPiece(int color) {
		if (color == Board.BLACK){
			addPiece(Piece.BLACK);
		} else {
			addPiece(Piece.WHITE);
		}
	}

}

package Model;

import java.util.ArrayList;

import Controller.BoardController;
import View.Board;
import gui.game.ColumnPanel;

public class Column {
	/**
	 * Constants for the state of each column.
	 */
	public static final int EMPTY = 0;
	public static final int BLACK = -1;
	public static final int WHITE = 1;

	ArrayList<Piece> pieces;
	int number;
	boolean isSelected;
	public boolean isHighlighted;
	public ColumnPanel panel;
	public Board board;
	public BoardController boardController;

	public Column(int i, Board board) {
		pieces = new ArrayList<>();		
		this.number = i;
		this.board = board;
	}
	public Column(int i, BoardController boardController) {
		pieces = new ArrayList<>();		
		this.number = i;
		this.boardController = boardController;
	}
	
	public ArrayList<Piece> getPieces(){
		return pieces;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isHighlighted() {
		return isHighlighted;
	}

	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}

	public ColumnPanel getPanel() {
		return panel;
	}

	public void setPanel(ColumnPanel panel) {
		this.panel = panel;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public static int getEmpty() {
		return EMPTY;
	}

	public static int getBlack() {
		return BLACK;
	}

	public static int getWhite() {
		return WHITE;
	}

	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public Column(ArrayList<Piece> pieces, int number, boolean isSelected, boolean isHighlighted, ColumnPanel panel,
			Board board) {
		super();
		this.pieces = pieces;
		this.number = number;
		this.isSelected = isSelected;
		this.isHighlighted = isHighlighted;
		this.panel = panel;
		this.board = board;
	}


	
}

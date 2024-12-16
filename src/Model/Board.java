package Model;

import View.Column;
import game.Piece;

public class Board {
	protected Column[] columns;
    public Column[] woodColumns;
    private int turn;

    private int[] dice;
    private int[] doubles;

    public static final int BLACK = -1;
    public static final int WHITE = 1;

    public static final int WOOD_WHITE = 26;
    public static final int WOOD_BLACK = 27;

    private Column selectedColumn;

    // Constructor
    public Board(int turn){
        columns = new Column[26];
        woodColumns = new Column[2];
        this.turn = turn;
    }

    // Getter and Setter methods for game state and dice
    public int[] getDice() {
        return dice;
    }
    public void setDice(int[] dice){
        this.dice = dice;
    }

    public int[] getDoubles() {
        return doubles;
    }
    public void setDoubles(int[] doubles){
        this.doubles = doubles;
    }

    public void changeTurn(){
        if (turn == BLACK){
            turn = WHITE;
        } else {
            turn = BLACK;
        }
    }

    public int getTurn(){
        return turn;
    }

    public Column getSelected(){
        return selectedColumn;
    }

    public Column[] getAll(){
        return columns;
    }

    public Column getWood(int color){
        return color == Model.Column.BLACK ? woodColumns[0] : woodColumns[1];
    }

	public Column[] getColumns() {
		return columns;
	}

	public void setColumns(Column[] columns) {
		this.columns = columns;
	}

	public Column[] getWoodColumns() {
		return woodColumns;
	}

	public void setWoodColumns(Column[] woodColumns) {
		this.woodColumns = woodColumns;
	}

	public Column getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelectedColumn(Column selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public static int getBlack() {
		return BLACK;
	}

	public static int getWhite() {
		return WHITE;
	}

	public static int getWoodWhite() {
		return WOOD_WHITE;
	}

	public static int getWoodBlack() {
		return WOOD_BLACK;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public Board(Column[] columns, Column[] woodColumns, int turn, int[] dice, int[] doubles, Column selectedColumn) {
		super();
		this.columns = columns;
		this.woodColumns = woodColumns;
		this.turn = turn;
		this.dice = dice;
		this.doubles = doubles;
		this.selectedColumn = selectedColumn;
	}

	public Board(Column[] columns, Column[] woodColumns) {
		super();
		this.columns = columns;
		this.woodColumns = woodColumns;
	}
	
	

   
    
   
}

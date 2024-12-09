package game;

/**
 * The representation of the board used by the main game.
 *
 */
public class Board {
	protected Column[] columns;
	public Column[] woodColumns;
	
	private int turn;
	
	/**
	 * The dice that the board currently holds.
	 */
	private int[] dice;
	private int[] doubles;
	
	/**
	 * Integers to represent the turn
	 */
	public static final int BLACK = -1;
	public static final int WHITE = 1;

	/**
	 * Constants to represent the unique number of the bar.
	 */
	public static final int WOOD_WHITE = 26;
	public static final int WOOD_BLACK = 27;

	/**
	 * Used by the player to make moves using the gui.
	 * Used by the AI to evalute moves.
	 * Used by the game logic to determine possible moves.
	 */
	private Column selectedColumn;
	
	public void setDice(int[] dice){
		this.dice = dice;
	}
	
	public int[] getDice(){
		return dice;
	}
	
	public void setDoubles(int[] doubles){
		this.doubles = doubles;
	}
	
	public int[] getDoubles(){
		return doubles;
	}
	
	public Board(int turn){
		columns = new Column[26];
		woodColumns = new Column[2];
		this.turn = turn;
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
		if (color == Column.BLACK){
			return woodColumns[0];
		} else {
			return woodColumns[1];
		}
	}
	
	public void init(){
		for (int i = 0; i < columns.length; i++){
			columns[i] = new Column(i,this);
		}

		for (int i = 0; i < woodColumns.length; i++){
			woodColumns[0] = new Column(WOOD_BLACK,this);
			woodColumns[1] = new Column(WOOD_WHITE,this);
		}
	}

	/**
	 * Populates the board using the default board state.
	 */
	public void addPieces(){

		columns[1].addPiece(Piece.WHITE); columns[1].addPiece(Piece.WHITE);

		columns[12].addPiece(Piece.WHITE); columns[12].addPiece(Piece.WHITE);
		columns[12].addPiece(Piece.WHITE); columns[12].addPiece(Piece.WHITE);
		columns[12].addPiece(Piece.WHITE);

		columns[17].addPiece(Piece.WHITE); columns[17].addPiece(Piece.WHITE);
		columns[17].addPiece(Piece.WHITE);

		columns[19].addPiece(Piece.WHITE); columns[19].addPiece(Piece.WHITE);
		columns[19].addPiece(Piece.WHITE); columns[19].addPiece(Piece.WHITE);
		columns[19].addPiece(Piece.WHITE);
		
		columns[6].addPiece(Piece.BLACK); columns[6].addPiece(Piece.BLACK);
		columns[6].addPiece(Piece.BLACK); columns[6].addPiece(Piece.BLACK);
		columns[6].addPiece(Piece.BLACK);
		
		columns[8].addPiece(Piece.BLACK); columns[8].addPiece(Piece.BLACK);
		columns[8].addPiece(Piece.BLACK);

		columns[13].addPiece(Piece.BLACK); columns[13].addPiece(Piece.BLACK);
		columns[13].addPiece(Piece.BLACK); columns[13].addPiece(Piece.BLACK);
		columns[13].addPiece(Piece.BLACK);

		columns[24].addPiece(Piece.BLACK); columns[24].addPiece(Piece.BLACK);

	}
	
	/**
	 * Sets the board state by using another board as a reference point.
	 * Used for AI calculations.
	 * @param other the other board.
	 */
	public void addPieces(Board other){
		for (int i = 0; i < columns.length; i++){
			for (int j = 0; j < other.getAll()[i].getPieces().size() - 1; j++){
				columns[i].addPiece(other.getAll()[i].getColor());
			}
		}

		for (int i = 0; i < woodColumns.length; i++){
			for (int j = 0; j < other.getAll()[i].getPieces().size(); j++){
				woodColumns[i].addPiece(other.getAll()[i].getPieces().get(0).getColor());
			}
		}
	}
	
	/**
	 * Finds a column in the board based on a move.
	 * @param move the move.
	 * @return the Column that is used.
	 */
	public Column findFrom(PossibleMove move){
		if (move.getFrom() == 0){
			return woodColumns[1];
		} else if (move.getFrom() == 25){
			return woodColumns[0];
		} else {
			return columns[move.getFrom()];
		}
	}

	/**
	 * Finds a column in the board based on a move.
	 * @param move the move.
	 * @return the Column that is used.
	 */
	public Column findTo(PossibleMove move){
		return columns[move.getTo()];
	}
	
	/**
	 * Finds a column based on its column number.
	 * @param i the column number.
	 * @return the Column that has the unique number.
	 */
	public Column find(int i) {
		if (i >= 0 && i < columns.length){
			return columns[i];			
		} else if (i == WOOD_BLACK){
			return woodColumns[0];
		} else if (i == WOOD_WHITE){
			return woodColumns[1];
		} else {
			return null;
		}

	}

	/**
	 * Checks if the selected column in this board is allowed to bear off.
	 * @return whether of not bearing off is possible.
	 */
	public boolean canBearOff(){
		boolean canBearOff = true;
		if (selectedColumn.getColor() == Column.BLACK){
			for (Column c : columns){
				if (c.getNumber() >= 7 && c.getColor() == Column.BLACK){
					canBearOff = false;
				}
			}
			for (Column w : woodColumns){
				if (w.getColor() == Column.BLACK){
					canBearOff = false;
				}
			}
		} else {
			for (Column c : columns){
				if (c.getNumber() <= 18 && c.getColor() == Column.WHITE){
					canBearOff = false;
				}
			}
			for (Column w : woodColumns){
				if (w.getColor() == Column.WHITE){
					canBearOff = false;
				}
			}
		}
		return canBearOff;
	}
	
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

	public void setSelected(Column column) {
		this.selectedColumn = column;
	}

	public void changeTurn(int turn) {
		this.turn = turn;
	}

	public void consumeDice(int i) {
		this.dice[i] = 0;
	}
	
	public void consumeDouble(int i){
		this.doubles[i] = 0;
	}
		
	public void setDiceFromDouble(int i){
		this.dice[i] = this.doubles[i];
		this.doubles[i] = 0;
	}
}

package game;

/**
 * Representation of a piece.
 *
 */
public class Piece {
	public static final boolean BLACK = true;
	public static final boolean WHITE = false;
	
	private boolean color;
	
	public Piece(boolean b) {
		this.color = b;
	}

	public boolean getColor(){
		return color;
	}
}

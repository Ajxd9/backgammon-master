package Model;

/**
 * Interface for all other AIs to implement.
 *
 */
public abstract interface AI {
	static final String[] aiNames = {"Random-chan", "Homura-chan", "Aoi-chan", "Miki-chan"};
	public static final int RandomIndex = 0;
	public static final int HomuraIndex = 1;
	public static final int AoiIndex = 2;
	public static final int MikiIndex = 3;
	
	/**
	 * Makes a move using the dice and the board belonging to the Game.
	 */
	public abstract void makeMove();
}

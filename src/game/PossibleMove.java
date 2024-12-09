package game;

/**
 * A possible move in the game given the dice rolls.
 *
 */
public class PossibleMove implements Cloneable{
	private int to,from,diceUsed;

	public PossibleMove(int to, int from, int diceUsed) {
		this.to = to;
		this.from = from;
		this.diceUsed = diceUsed;
	}

	public int getTo() {
		return to;
	}

	public int getFrom() {
		return from;
	}

	public int getDiceUsed() {
		return diceUsed;
	}
	
	public PossibleMove clone(){
		return new PossibleMove(this.to, this.from, this.diceUsed);
	}
	
}

package View;

import java.util.HashMap;

/**
 * A list of probabilities that each move can occur.
 * Unused.
 *
 */
public class MoveProbability {
	
	//nth Index of the array is P(X=n)
	//0th index is the sum of the occurrences.
	public static final int[] moveProbabilities = {
		114,
		11,
		12,
		14,
		15,
		15,
		17,
		6,
		6,
		5,
		3,
		2,
		3,
		0,
		0,
		1,
		1,
		0,
		1,
		0,
		0,
		1,
		0,
		0,
		0,
		1
	};
	
	public static double getMoveProbability(int i){
		return moveProbabilities[i]/(1.0*moveProbabilities[0]);
	}
}


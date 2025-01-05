import org.junit.jupiter.api.Test;

import com.HyenaBgammon.models.AssistantLevel;
import com.HyenaBgammon.models.DieType;
import com.HyenaBgammon.models.Game;
import com.HyenaBgammon.models.GameDifficulty;
import com.HyenaBgammon.models.GameParameters;
import com.HyenaBgammon.models.Player;
import com.HyenaBgammon.models.SixSidedDie;
import com.HyenaBgammon.models.SquareColor;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;


// Test class for rollDice in the Game class
public class DiceRollingTest {

    @Test
    public void testRollDiceAllDifficulties() {
        // Test for all difficulties
        testRollDice(GameDifficulty.EASY, 2, 4);
        testRollDice(GameDifficulty.MEDIUM, 3, 5);
        testRollDice(GameDifficulty.HARD, 3, 5);
    }

    private void testRollDice(GameDifficulty difficulty, int expectedDiceCount, int expectedDiceCountWithDoubles) {
        // Setup GameParameters and Game
        GameParameters gameParameters = new GameParameters(
            60,                              // secondsPerTurn
            3,                               // winningGamesCount
            false,                           // useDoubling
            difficulty,                      // game difficulty
            new Player(1, "Player 1", "", AssistantLevel.NOT_USED), // white player
            new Player(2, "Player 2", "", AssistantLevel.NOT_USED)  // black player
        );

        Game game = new Game(gameParameters);
        game.setCurrentPlayer(SquareColor.WHITE);

        // Execute rollDice method
        game.rollDice();

        // Retrieve the rolled dice
        List<SixSidedDie> dice = game.getSixSidedDie();

        // Validate dice list
        assertNotNull(dice, "Dice list should not be null");
        assertEquals(expectedDiceCount, dice.size(), "Expected " + expectedDiceCount + " dice for " + difficulty + " difficulty before doubles");

        // Check doubles logic
        if (dice.get(0).getValue() == dice.get(1).getValue()) {
            assertEquals(expectedDiceCountWithDoubles, dice.size(), "Expected " + expectedDiceCountWithDoubles + " dice if doubles are rolled in " + difficulty + " difficulty");
        }

        // Validate game state
        assertFalse(game.isTurnFinished(), "Turn should not be finished after rolling dice");
    }
    
    @Test
    public void testRollForAllDieTypes() {
        // Test each die type
        testDieRoll(DieType.REGULAR, 1, 6);       // Regular dice: Values 1-6
        testDieRoll(DieType.ENHANCED, -3, 6);    // Enhanced dice: Values -3 to 6
        testDieRoll(DieType.QUESTION, 0, 1, 3);  // Question dice: Values 1-3 for diffValue
    }

    private void testDieRoll(DieType dieType, int expectedMin, int expectedMax) {
        SixSidedDie die = new SixSidedDie(dieType, SquareColor.WHITE);
        die.roll();

        int value = die.getValue();
        assertTrue(value >= expectedMin && value <= expectedMax, "Die value " + value + " not in range " + expectedMin + " to " + expectedMax + " for " + dieType);
    }

    private void testDieRoll(DieType dieType, int expectedValue, int diffMin, int diffMax) {
        SixSidedDie die = new SixSidedDie(dieType, SquareColor.WHITE);
        die.roll();

        int value = die.getValue();
        int diffValue = die.getDiffValue();

        assertEquals(expectedValue, value, "Expected value for " + dieType + " die is " + expectedValue);
        assertTrue(diffValue >= diffMin && diffValue <= diffMax, "Diff value " + diffValue + " not in range " + diffMin + " to " + diffMax + " for " + dieType);
    }
}

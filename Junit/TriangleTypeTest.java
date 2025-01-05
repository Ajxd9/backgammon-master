import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.HyenaBgammon.models.*;

public class TriangleTypeTest {

    @Test
    public void testGetTriangleType() {
        // Test for QUESTION square
        Square questionSquare = new Square(SquareColor.WHITE, 0, 1, SquareType.QUESTION);
        assertEquals("QUESTION", getTriangleType(questionSquare), "Expected type: QUESTION");

        // Test for SURPRISE square
        Square surpriseSquare = new Square(SquareColor.BLACK, 0, 2, SquareType.SURPRISE);
        assertEquals("SURPRISE", getTriangleType(surpriseSquare), "Expected type: SURPRISE");

        // Test for REGULAR square
        Square regularSquare = new Square(SquareColor.EMPTY, 0, 3, SquareType.REGULAR);
        assertEquals("REGULAR", getTriangleType(regularSquare), "Expected type: REGULAR");

        // Test for edge case: null squareType
        Square nullSquareType = new Square(SquareColor.EMPTY, 0, 4);
        nullSquareType.setSquareType(null);
        assertEquals("REGULAR", getTriangleType(nullSquareType), "Expected type: REGULAR for null squareType");
    }

    // Method under test
    private String getTriangleType(Square square) {
        if (square.getSquareType() == SquareType.QUESTION) {
            return "QUESTION";
        } else if (square.getSquareType() == SquareType.SURPRISE) {
            return "SURPRISE";
        } else {
            return "REGULAR";
        }
    }
}

import org.junit.Test;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Puzzle;

import static junit.framework.TestCase.*;

public class WebMoveTest {

    @Test
    public void testMove(){
        Field field = new Field(3, 3);
        field.moveWeb("23");
        Puzzle[][] puzzles = field.getPuzzles();
        assertNull(puzzles[1][2]);
        field.moveWeb("33");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
        field.moveWeb("32");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][1]);
        field.moveWeb("33");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
    }

    @Test
    public void testDoubleMove(){
        Field field = new Field(3, 3);
        field.moveWeb("13");
        Puzzle[][] puzzles = field.getPuzzles();
        assertNull(puzzles[0][2]);
        field.moveWeb("11");
        puzzles = field.getPuzzles();
        assertNull(puzzles[0][0]);
        field.moveWeb("31");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][0]);
        field.moveWeb("33");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
    }
}

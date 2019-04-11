import org.junit.Test;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Gamestate;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Puzzle;

import static junit.framework.TestCase.*;

public class WebMoveTest {
    private Field field;
    private Puzzle[][] puzzles;


    @Test
    public void testMoveWeb(){
        field = new Field(3,3);
        field.moveWeb("12");
        puzzles = field.getPuzzles();
        assertNull(puzzles[1][2]);
        field.moveWeb("22");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
        field.moveWeb("21");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][1]);
        field.moveWeb("22");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
    }

}

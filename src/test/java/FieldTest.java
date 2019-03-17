import org.junit.Test;
import sk.tuke.gamestudio.pictureslidingpuzzle.core.Field;
import sk.tuke.gamestudio.pictureslidingpuzzle.core.Gamestate;
import sk.tuke.gamestudio.pictureslidingpuzzle.core.Puzzle;

import static junit.framework.TestCase.*;

public class FieldTest {
    private Field field;
    private Puzzle[][] puzzles;

    @Test
    public void testGenerate(){
        field = new Field(3,3);
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
    }

    @Test
    public void testIsWon(){
        field = new Field(3,3);
        assertTrue(field.isSolved());
        field.shuffle(5);
        assertFalse(field.isSolved());
    }

    @Test
    public void testMove(){
        field = new Field(3,3);
        field.move("DOWN");
        puzzles = field.getPuzzles();
        assertNull(puzzles[1][2]);
        field.move("UP");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
        field.move("RIGHT");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][1]);
        field.move("LEFT");
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
    }

    @Test
    public void testSetState(){
        field = new Field(3,3);
        Gamestate gamestate = Gamestate.PLAYING;
        field.setState(Gamestate.PLAYING);
        assertEquals(gamestate,field.getState());
    }
}

import org.junit.Test;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Gamestate;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Puzzle;

import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.TestCase.*;

public class FieldTest {
    private Field field;
    private Puzzle[][] puzzles;

    @Test
    public void testGenerate() throws IOException, URISyntaxException {
        field = new Field(3,3);
        puzzles = field.getPuzzles();
        assertNull(puzzles[2][2]);
    }

    @Test
    public void testIsWon() throws IOException, URISyntaxException {
        field = new Field(3,3);
        assertTrue(field.isSolved());
        field.shuffle(5);
        assertFalse(field.isSolved());
    }

    @Test
    public void testMove() throws IOException, URISyntaxException {
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
    public void testSetState() throws IOException, URISyntaxException {
        field = new Field(3,3);
        Gamestate gamestate = Gamestate.PLAYING;
        field.setState(Gamestate.PLAYING);
        assertEquals(gamestate,field.getState());
    }
}

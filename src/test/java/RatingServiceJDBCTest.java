import org.junit.Test;
import sk.tuke.gamestudio.pictureslidingpuzzle.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingServiceJDBC;
import sk.tuke.gamestudio.service.ScoreException;


public class RatingServiceJDBCTest extends RatingServiceTest{
    public RatingServiceJDBCTest(){
        super.ratingService = new RatingServiceJDBC();
    }

    @Test
    public void testDbInit() throws Exception {
        super.testDbInit();
    }

    @Test
    public void testSetRating() throws Exception {
        super.testSetRating();
    }

    @Test
    public void testAverageRating() throws Exception {
        super.testAverageRating();
    }

    public static class PictureSlidingPuzzle {
        public static void main(String[] args) throws CommentException, RatingException, ScoreException {
            ConsoleUI ui = new ConsoleUI();
            ui.run();
        }
    }
}

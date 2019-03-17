import org.junit.Test;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;


public class ScoreServiceJDBCTest extends ScoreServiceTest {
    public ScoreServiceJDBCTest() {
        super.scoreService = new ScoreServiceJDBC();
    }

    @Test
    public void testDbInit() throws ScoreException {
        super.testDbInit();
    }

    @Test
    public void testAddScore() throws ScoreException {
        super.testAddScore();
    }

    @Test
    public void testGetBestScores() throws ScoreException {
        super.testGetBestScores();
    }

}

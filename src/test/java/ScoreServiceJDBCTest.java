import org.junit.Test;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;


public class ScoreServiceJDBCTest extends ScoreServiceTest {
    public ScoreServiceJDBCTest() {
        super.scoreService = new ScoreServiceJDBC();
    }

    @Test
    public void testDbInit() throws Exception {
        super.testDbInit();
    }

    @Test
    public void testAddScore() throws Exception {
        super.testAddScore();
    }

    @Test
    public void testGetBestScores() throws Exception {
        super.testGetBestScores();
    }

}

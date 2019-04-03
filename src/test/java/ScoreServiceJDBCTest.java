import org.junit.Test;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreServiceJDBC;

import java.sql.SQLException;


public class ScoreServiceJDBCTest extends ScoreServiceTest {
    public ScoreServiceJDBCTest() {
        super.scoreService = new ScoreServiceJDBC();
    }

    @Test
    public void testDbInit() throws ScoreException {
        super.testDbInit();
    }

    @Test
    public void testAddScore() throws ScoreException, SQLException {
        super.testAddScore();
    }

    @Test
    public void testGetBestScores() throws ScoreException, SQLException {
        super.testGetBestScores();
    }

}

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;
import sk.tuke.gamestudio.server.service.ScoreServiceJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field.GAME_NAME;
public class ScoreServiceTest{
    ScoreService scoreService = new ScoreServiceJDBC();

    private static final String DELETE = "DELETE FROM score where game = 'Picture Sliding Puzzle'";

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    @Before@After
    public void setUp() throws Exception {
        try(Connection c = DriverManager.getConnection(URL, USER, PASS)) {
            try (Statement s = c.createStatement()){
                s.execute(DELETE);
            }

        }
        catch (Exception e){
            throw new Exception();
        }
    }

    @Test
    public void testDbInit() throws ScoreException {
        assertEquals(0, scoreService.getBestScores(GAME_NAME).size());
    }

    @Test
    public void testAddScore() throws ScoreException, SQLException {
        Score score = new Score(GAME_NAME, "miska", 15, new Date());
        scoreService.addScore(score);
        assertEquals(1, scoreService.getBestScores(GAME_NAME).size());
    }

    @Test
    public void testGetBestScores() throws ScoreException, SQLException {
        Score s1 = new Score(GAME_NAME, "janko", 150, new Date());
        Score s2 = new Score(GAME_NAME, "hrasko", 300, new Date());

            scoreService.addScore(s1);
            scoreService.addScore(s2);

        List<Score> scores = scoreService.getBestScores(GAME_NAME);
        assertEquals(s2.getPoints(), scores.get(0).getPoints());
        assertEquals(s2.getPlayer(), scores.get(0).getPlayer());
    }
}

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static sk.tuke.gamestudio.pictureslidingpuzzle.core.Field.GAME_NAME;


public class RatingServiceTest {

    RatingService ratingService = new RatingServiceJDBC();
    private static final String DELETE = "DELETE FROM rating";

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";


    @Before@After
    public void setUp() throws Exception {
        Connection c = DriverManager.getConnection(URL, USER, PASS);
        Statement s = c.createStatement();
        s.execute(DELETE);
    }

    @Test
    public void testDbInit() throws RatingException {
        assertEquals(0, ratingService.getRating(GAME_NAME,""));
    }

    @Test
    public void testSetRating() throws RatingException {
        Rating rating = new Rating("miska",GAME_NAME,  5, new Date());
        ratingService.setRating(rating);
        assertEquals(5, ratingService.getRating(GAME_NAME,"miska"));
    }
    @Test
    public void testAverageRating() throws RatingException {
        Rating r2 = new Rating("miska", GAME_NAME, 7, new Date());
        Rating r1 = new Rating("jarko", GAME_NAME, 3, new Date());

        ratingService.setRating(r1);
        ratingService.setRating(r2);

        assertEquals(5, ratingService.getAverageRating(GAME_NAME));
    }

}

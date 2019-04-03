import org.junit.Test;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingServiceJDBC;


public class RatingServiceJDBCTest extends RatingServiceTest{
    public RatingServiceJDBCTest(){
        super.ratingService = new RatingServiceJDBC();
    }

    @Test
    public void testDbInit() throws RatingException {
        super.testDbInit();
    }

    @Test
    public void testSetRating() throws RatingException {
        super.testSetRating();
    }

    @Test
    public void testAverageRating() throws RatingException {
        super.testAverageRating();
    }

}

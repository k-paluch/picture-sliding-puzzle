import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)

@Suite.SuiteClasses({
        CommentServiceTest.class,
        CommentServiceJDBCTest.class,
        FieldTest.class,
        RatingServiceTest.class,
        RatingServiceJDBCTest.class,
        ScoreServiceTest.class,
        ScoreServiceJDBCTest.class
})
public class TestSuite {

}

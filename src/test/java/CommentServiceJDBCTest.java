import org.junit.Test;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

public class CommentServiceJDBCTest extends CommentServiceTest {
    public CommentServiceJDBCTest(){
        super.commentService = new CommentServiceJDBC();
    }

    @Test
    public void testDbInit() throws CommentException {
        super.testDbInit();
    }

    @Test
    public void testAddComment() throws CommentException {
        super.testAddComment();
    }

    @Test
    public void testGetComments() throws CommentException {
        super.testGetComments();
    }

}

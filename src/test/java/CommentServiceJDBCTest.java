import org.junit.Test;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

public class CommentServiceJDBCTest extends CommentServiceTest {
    public CommentServiceJDBCTest(){
        super.commentService = new CommentServiceJDBC();
    }

    @Test
    public void testDbInit() throws Exception {
        super.setUp();
    }

    @Test
    public void testAddComment() throws Exception{
        super.testAddComment();
    }

    @Test
    public void testGetComments() throws Exception {
        super.testGetComments();
    }

}

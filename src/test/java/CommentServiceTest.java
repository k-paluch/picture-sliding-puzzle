import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.CommentServiceJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field.GAME_NAME;
public class CommentServiceTest {
    CommentService commentService = new CommentServiceJDBC();
    private static final String DELETE = "DELETE FROM comment WHERE game = 'Picture Sliding Puzzle'";

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
    public void testDbInit() throws CommentException {
        assertEquals(0, commentService.getComments(GAME_NAME).size());
    }

    @Test
    public void testAddComment() throws CommentException {
        Comment comment = new Comment("miska", GAME_NAME, "Super hra, odporucam", new Date());
        commentService.addComment(comment);
        assertEquals(1, commentService.getComments(GAME_NAME).size());
    }

    @Test
    public void testGetComments() throws CommentException {
        Comment c1 = new Comment("miska", GAME_NAME, "perfect game", new Date());
        Comment c2 = new Comment("hrasko", GAME_NAME, "najuzauzasnejsia igra v celej jugoslavii", new Date());

        commentService.addComment(c1);
        commentService.addComment(c2);

        List<Comment> comments = commentService.getComments(GAME_NAME);
        assertEquals(c2.getComment(), comments.get(1).getComment());
    }
}

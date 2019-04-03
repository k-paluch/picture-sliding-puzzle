package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch;

import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.consoleui.ConsoleUI;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.RatingException;

import java.sql.SQLException;

public class PictureSlidingPuzzle {
    public static void main(String[] args) throws CommentException, RatingException, SQLException {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}
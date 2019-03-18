package sk.tuke.gamestudio.pictureslidingpuzzle;

import sk.tuke.gamestudio.pictureslidingpuzzle.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.RatingException;

import java.sql.SQLException;

public class PictureSlidingPuzzle {
    public static void main(String[] args) throws CommentException, RatingException, SQLException {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}

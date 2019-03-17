package sk.tuke.gamestudio.pictureslidingpuzzle;

import sk.tuke.gamestudio.pictureslidingpuzzle.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.RatingException;

public class PictureSlidingPuzzle {
    public static void main(String[] args) throws CommentException, RatingException {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}

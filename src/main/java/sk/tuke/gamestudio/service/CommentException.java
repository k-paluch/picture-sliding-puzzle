package sk.tuke.gamestudio.service;

public class CommentException extends Exception {
    public CommentException(String message) {
        super(message);
    }

    CommentException(String message, Throwable cause) {
        super(message, cause);
    }
}

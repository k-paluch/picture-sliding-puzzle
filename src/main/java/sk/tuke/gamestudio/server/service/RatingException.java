package sk.tuke.gamestudio.server.service;

public class RatingException extends Exception {
    public RatingException(String message) {
        super(message);
    }

    RatingException(String message, Throwable cause) {
        super(message, cause);
    }
}

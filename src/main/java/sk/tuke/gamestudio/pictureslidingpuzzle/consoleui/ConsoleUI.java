package sk.tuke.gamestudio.pictureslidingpuzzle.consoleui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.pictureslidingpuzzle.core.Difficulty;
import sk.tuke.gamestudio.pictureslidingpuzzle.core.Field;
import sk.tuke.gamestudio.pictureslidingpuzzle.core.Gamestate;
import sk.tuke.gamestudio.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sk.tuke.gamestudio.pictureslidingpuzzle.core.Field.GAME_NAME;

public class ConsoleUI {
    private final Pattern INPUT_PATTERN = Pattern.compile(
            "([Uu][Pp]|[Dd][Oo][Ww][Nn]|[Ll][Ee][Ff][Tt]|[Rr][Ii][Gg][Hh][Tt])"
    );
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private Field field;

    public ConsoleUI() {
    }

    private void generate() {
        System.out.println("Choose difficulty (EASY, MEDIUM, HARD):");
        String line = readLine();
        if (line.equalsIgnoreCase("easy")) {
            field = new Field(3, 3);
            field.setDifficulty(Difficulty.EASY);
            field.shuffle(5);
        } else if (line.equalsIgnoreCase("medium")) {
            field = new Field(4, 4);
            field.setDifficulty(Difficulty.MEDIUM);
            field.shuffle(100);
        } else if (line.equalsIgnoreCase("hard")) {
            field = new Field(5, 5);
            field.setDifficulty(Difficulty.HARD);
            field.shuffle(200);
        } else {
            System.out.println("Neplatny vstup skus znova!");
            generate();
        }
        field.setAlreadyShuffled();
    }

    private String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            System.err.println("Nepodarilo sa nacitat vstup, skus znova");
            return "";
        }
    }

    private void processInput() {
        System.out.println("Make move (UP, DOWN, LEFT, RiGHT):");
        System.out.printf("You've been playing for: %d seconds %n ", field.getPlayingTime());
        String line = readLine();
        Matcher m = INPUT_PATTERN.matcher(line);
        if (m.matches()) {
            field.move(line);
        } else {
            System.out.println("Nezadal si dobry vstup, skus znova.");
        }
    }

    private void comment() throws CommentException {
        System.out.println("Your comment:");
        String commentString = readLine();
        System.out.println("Enter your name");
        String name = readLine();
        Date date = new Date();
        Comment comment = new Comment(name, GAME_NAME, commentString, date);
        CommentService commentService = new CommentServiceJDBC();
        commentService.addComment(comment);
        System.out.println("Your comment was added to database");
    }

    private void showAverageRating() throws RatingException {
        RatingService ratingService = new RatingServiceJDBC();
        System.out.println("The average rating is: " + ratingService.getAverageRating(GAME_NAME));
    }

    private void rate(Date date) throws RatingException {
        System.out.println("Your rating (0-5) stars");
        int rate = Integer.valueOf(readLine());
        do {
            if (rate <= 5 && rate >= 0) {
                System.out.println("Enter your name");
                String name = readLine();
                Rating rating = new Rating(name, GAME_NAME, rate, date);
                RatingService ratingService = new RatingServiceJDBC();
                ratingService.setRating(rating);
            } else {
                System.out.println("Wrong rating");
                rate = Integer.valueOf(readLine());
            }
        } while (!(rate <= 5 && rate >= 0));
    }

    private void saveScore() throws SQLException {
        System.out.println("YOU WON!");
        int points = field.getScore();
        System.out.printf("Your score was: %d%n", points);
        System.out.println("Enter your name");
        String name = readLine();
        Date date = new Date();
        Score score = new Score(GAME_NAME, name, points, date);
        ScoreService scoreService = new ScoreServiceJDBC();
        List<Score> scores = scoreService.getBestScores(GAME_NAME);
        scoreService.addScore(score);
        if (scores.isEmpty()) {
            System.out.println("New highscore but don't be so happy about it, u're the first to play this game.");
        } else if (score.getPoints() > scores.get(0).getPoints()) {
            System.out.println("NEW HIGHSCORE!!!! CONGRATULATIONS!!!");

        }
    }

    private void printHighscore() {
        System.out.println("Score \t Date \t \t \t\t\tPlayer ");
        ScoreService scoreService = new ScoreServiceJDBC();
        List<Score> scores = scoreService.getBestScores(GAME_NAME);
        for (Score s : scores) {
            System.out.println(s.toString());
        }
    }

    private void showComments() throws CommentException {
        CommentService commentService = new CommentServiceJDBC();
        List<Comment> comments = commentService.getComments(GAME_NAME);
        for (Comment c : comments) {
            System.out.println(c.toString());
            System.out.println();
        }
    }

    public void run() throws CommentException, RatingException, SQLException {
        printMenu();

        String line = readLine();
        while (!line.equalsIgnoreCase("X")) {
            Date date = new Date();
            if (line.equalsIgnoreCase("w")) {
                comment();
            } else if (line.equalsIgnoreCase("r")) {
                rate(date);

            } else if (line.equalsIgnoreCase("p")) {
                this.generate();
                this.play();
            } else if (line.equalsIgnoreCase("t")) {
                printHighscore();
            } else if (line.equalsIgnoreCase("s")) {
                showComments();
            } else if (line.equalsIgnoreCase("a")) {
                showAverageRating();
            }
            printMenu();
            line = readLine();
        }
        System.out.println("Bye have a great time!");
    }

    private void printMenu() {
        System.out.println("<P> Play");
        System.out.println("<R> Rate");
        System.out.println("<A> Show average rating");
        System.out.println("<T> Top 10");
        System.out.println("<S> Show comments");
        System.out.println("<W> Write a comment");
        System.out.println("<X> Exit");
    }

    private void play() throws CommentException, RatingException, ScoreException, SQLException {
        do {
            field.render();
            processInput();
            if (field.isSolved()) {
                field.setState(Gamestate.SOLVED);
            }
        } while (field.getState() == Gamestate.PLAYING);
        field.render();
        saveScore();
        run();
    }
}

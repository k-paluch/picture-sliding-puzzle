package sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Difficulty;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Gamestate;
import sk.tuke.gamestudio.server.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field.GAME_NAME;

public class ConsoleUI {
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private Date date = new Date();

    private final Pattern INPUT_RATE_PATTERN = Pattern.compile("[0-5]");
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private Field field;
    private int rowcol;
    private final Pattern INPUT_PATTERN = Pattern.compile(
            "([0-{rowcol}][0-{rowcol}])"
    );
    public ConsoleUI() {
    }

    private void generate() throws IOException, URISyntaxException {
        System.out.println("Choose difficulty:");
        System.out.println("<E> EASY");
        System.out.println("<M> MEDIUM");
        System.out.println("<H> HARD");
        String line = readLine();
        if (line.equalsIgnoreCase("E")) {
            field = new Field(3, 3);
            field.setDifficulty(Difficulty.EASY);
            field.shuffle(5);
            rowcol = 3;
        } else if (line.equalsIgnoreCase("M")) {
            field = new Field(4, 4);
            field.setDifficulty(Difficulty.MEDIUM);
            field.shuffle(100);
            rowcol=4;
        } else if (line.equalsIgnoreCase("H")) {
            field = new Field(5, 5);
            field.setDifficulty(Difficulty.HARD);
            field.shuffle(200);
            rowcol = 5;
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

    private void processInput() throws RatingException, SQLException, CommentException, IOException, URISyntaxException {
        System.out.println("<[1-" + rowcol + "][1-" + rowcol + "]> Make move");
        System.out.println("<X> Exit");
        System.out.printf("You've been playing for: %d seconds %n ", field.getPlayingTime());
        String line = readLine();
        if(line.equalsIgnoreCase("X")){
            run();
        }
        Matcher m = INPUT_PATTERN.matcher(line);
        if (m.matches()) {
            //field.move(line);
            field.moveWeb(line);
            
        } else {
            System.out.println("Nezadal si dobry vstup, skus znova.");
        }
    }

    private void comment() throws CommentException {
        System.out.println("Your comment:");
        String commentString = readLine();
        System.out.println("Enter your name");
        String name = readLine();
        Comment comment = new Comment(name, GAME_NAME, commentString, date);
        commentService.addComment(comment);
        System.out.println("Your comment was added to database");
    }

    private void showAverageRating() throws RatingException {
        System.out.println("The average rating is: " + ratingService.getAverageRating(GAME_NAME));
    }

    private void rate() throws RatingException {
        System.out.println("Your rating (0-5) stars");
        String rate = readLine();
        Matcher m = INPUT_RATE_PATTERN.matcher(rate);
                if(m.matches()) {
                    if (Integer.parseInt(rate) <= 5 && Integer.parseInt(rate) >= 0) {
                        System.out.println("Enter your name");
                        String name = readLine();
                        Rating rating = new Rating(name, GAME_NAME, Integer.parseInt(rate), date);
                        ratingService.setRating(rating);
                    }
                }else {
                    System.out.println("Wrong rating");
                    rate();
                }
    }

    private void saveScore() throws SQLException {
        System.out.println("YOU WON!");
        int points = field.getScore();
        System.out.printf("Your score was: %d%n", points);
        System.out.println("Enter your name");
        String name = readLine();
        Score score = new Score(GAME_NAME, name, points, date);
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
        List<Score> scores = scoreService.getBestScores(GAME_NAME);
        if(scores.isEmpty()){
            System.out.println("No score to show");
            return;
        }
        for (Score s : scores) {
            System.out.println(s.toString());
        }
    }

    private void showComments() throws CommentException {
        List<Comment> comments = commentService.getComments(GAME_NAME);
        if(comments.isEmpty()){
            System.out.println("No comments to show");
            return;
        }
        for (Comment c : comments) {
            System.out.println(c.toString());
            System.out.println();
        }
    }

    public void run() throws CommentException, RatingException, SQLException, IOException, URISyntaxException {
        printMenu();

        String line = readLine();
        while (!line.equalsIgnoreCase("X")) {
            if (line.equalsIgnoreCase("w")) {
                comment();
            } else if (line.equalsIgnoreCase("r")) {
                rate();

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
        System.exit(0);
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

    public void play() throws CommentException, RatingException, ScoreException, SQLException, IOException, URISyntaxException {
        if (field != null) {
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
}

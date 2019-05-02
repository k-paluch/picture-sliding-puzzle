package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Difficulty;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Gamestate;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Puzzle;

import java.io.IOException;
import java.net.URISyntaxException;


@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PictureSlidingPuzzleController {
    private Field field ;
    private Puzzle[][] puzzles ;
    private Difficulty difficulty = Difficulty.EASY;
    private boolean diffChosen = false;
    @RequestMapping("/pictureslidingpuzzle")
    public String pictureslidingpuzzle(String row, String col) {
        System.out.println(row + col);
        if(field!=null) {
            try {
                field.moveWeb(row + col);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < field.getRowCount(); i++) {
                for (int j = 0; j < field.getColumnCount(); j++) {
                    if(puzzles[i][j]==null){
                        System.out.println("null");
                    }
                    else {
                        System.out.print(Integer.toString(i) + j);
                        System.out.println(puzzles[i][j].imgValue());
                    }
                }
            }
        }
        return "pictureslidingpuzzle";
    }

    public Gamestate getGameState() {
        return field!=null ? field.getState() : null;
    }

    public Difficulty getDifficulty() {
        return field!=null ? field.getDifficulty(): null;
    }

    @RequestMapping("/pictureslidingpuzzle/new")
    public String newGame() throws IOException, URISyntaxException {
        if(diffChosen) {
            if (difficulty == Difficulty.EASY) {
                field = new Field(3, 3);
                field.shuffle(5);
            } else if (difficulty == Difficulty.MEDIUM) {
                field = new Field(4, 4);
                field.shuffle(50);
            } else if (difficulty == Difficulty.HARD) {
                field = new Field(5, 5);
                field.shuffle(100);
            }
            this.puzzles = field.getPuzzles();
        }
        return "pictureslidingpuzzle";
    }

    @RequestMapping("/pictureslidingpuzzle/easy")
    public String easyD() throws IOException, URISyntaxException {
        field = new Field(3, 3);
        difficulty = Difficulty.EASY;
        field.setDifficulty(difficulty);
        field.shuffle(5);
        puzzles = field.getPuzzles();
        diffChosen = true;
        return "pictureslidingpuzzle";
    }

    @RequestMapping("/pictureslidingpuzzle/medium")
    public String mediumD() throws IOException, URISyntaxException {
        field = new Field(4, 4);
        difficulty = Difficulty.MEDIUM;
        field.setDifficulty(difficulty);
        field.shuffle(50);
        puzzles = field.getPuzzles();
        diffChosen = true;
        return "pictureslidingpuzzle";
    }

    @RequestMapping("/pictureslidingpuzzle/hard")
    public String hard() throws IOException, URISyntaxException {
        field = new Field(5, 5);
        difficulty = Difficulty.HARD;
        field.setDifficulty(difficulty);
        field.shuffle(100);
        puzzles = field.getPuzzles();
        diffChosen = true;
        return "pictureslidingpuzzle";
    }


    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        if(field!=null) {
            if (!field.isSolved()) {
                if (field != null) {
                    sb.append("<table>\n");
                    for (int row = 0; row < field.getRowCount(); row++) {
                        sb.append("<tr>\n");
                        for (int col = 0; col < field.getColumnCount(); col++) {
                            sb.append("<td>\n");
                            if(puzzles[row][col]!=null) {
                                sb.append("<a href='" + String.format("/pictureslidingpuzzle?row=%s&col=%s", row, col)
                                        + "'>\n");
                                sb.append("<img src='/images/pictureslidingpuzzle/" + puzzles[row][col].imgValue() + ".png' height='" + 500 / field.getRowCount() + "' width= '" + 500 / field.getColumnCount() + "'>");
                                sb.append("</a>\n");
                            }
                            else{
                                sb.append("<img src='/images/pictureslidingpuzzle/null.png' height='" + 500 / field.getRowCount() + "' width= '" + 500 / field.getColumnCount() + "'>");
                            }
                            sb.append("</td>\n");
                        }
                        sb.append("</tr>\n");
                    }
                    sb.append("</table>\n");
                    return sb.toString();
                }
            } else if (!diffChosen) {
                sb.append("Please choose a difficulty to start the game my mastah");
                return sb.toString();
            } else {
                sb.append("You won");
                return sb.toString();
            }
        }
        return "pictureslidingpuzzle";
    }
}
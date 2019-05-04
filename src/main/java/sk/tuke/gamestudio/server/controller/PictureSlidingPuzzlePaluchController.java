package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Difficulty;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Field;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Gamestate;
import sk.tuke.gamestudio.game.pictureslidingpuzzle.paluch.core.Puzzle;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreService;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/pictureslidingpuzzle-paluch")
public class PictureSlidingPuzzlePaluchController {
    private static Map<Integer, Field> fields = new HashMap<>();

    @Autowired
    ServletContext servletContext;

    @Autowired
    private ScoreService scoreService;

    private Field field ;
    private Puzzle[][] puzzles ;
    private Difficulty difficulty = Difficulty.EASY;
    private boolean diffChosen = false;
    private String loggedUser;

    private int displayId;

    @RequestMapping
    public String pictureslidingpuzzle(String row, String col, Model model) {
        if(field!=null) {
            if(!field.isSolved()) {
                try {
                    field.moveWeb(row + col);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }


    public String getLoggedUser(){
        return loggedUser;
    }


    @RequestMapping("/field")
    public String field(String row, String col,Model model) {
        System.out.println(row + col);
        if(field!=null) {
            try {
                field.moveWeb(row + col);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        updateModel(model);
        return "field";
    }

    public Gamestate getGameState() {
        return field!=null ? field.getState() : null;
    }

    public Difficulty getDifficulty() {
        return field!=null ? field.getDifficulty(): null;
    }

    @RequestMapping("/new")
    public String newGame(Model model) throws IOException, URISyntaxException {
        field = null;
        difficulty=null;
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
            if(this.field!=null)
            this.puzzles = field.getPuzzles();
        }
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/easy")
    public String easyD(Model model) throws IOException, URISyntaxException {
        field = new Field(3, 3);
        difficulty = Difficulty.EASY;
        field.setDifficulty(difficulty);
        field.shuffle(5);
        puzzles = field.getPuzzles();
        diffChosen = true;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/medium")
    public String mediumD(Model model) throws IOException, URISyntaxException {
        field = new Field(4, 4);
        difficulty = Difficulty.MEDIUM;
        field.setDifficulty(difficulty);
        field.shuffle(50);
        puzzles = field.getPuzzles();
        diffChosen = true;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/hard")
    public String hard(Model model) throws IOException, URISyntaxException {
        field = new Field(5, 5);
        difficulty = Difficulty.HARD;
        field.setDifficulty(difficulty);
        field.shuffle(100);
        puzzles = field.getPuzzles();
        diffChosen = true;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/login")
    public String login(String login, Model model) {
        loggedUser = login;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/logout")
    public String logout(Model model) {
        loggedUser = null;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    private void newGame() throws IOException, URISyntaxException {
        field = null;
        difficulty=null;
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
        if (displayId != 0)
            fields.put(displayId, field);
    }

    @RequestMapping("/display/{id}")
    public String display(@PathVariable int id, Model model) throws IOException {
        displayId = id;
        Field field = fields.get(id);
        if (field != null)
            model.addAttribute("htmlField", getHtmlField(field));
        updateModel(model);
        return "pictureslidingpuzzle-paluch_view";
    }

    public int getDisplayId(){
        return this.displayId;
    }

    public Field getField(){
        return this.field;
    }

    public List<Score> getBestScores (){
        return scoreService.getBestScores("pictureslidingpuzzle");
    }

    private void updateModel(Model model) {
        model.addAttribute("displayId", displayId);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("scores", scoreService.getBestScores("pictureslidingpuzzle"));
    }
    public String getHtmlField() throws IOException {
        return getHtmlField(field);
    }
    @RequestMapping("/display/pair/{id}")
    public String pair(@PathVariable int id, Model model) throws IOException, URISyntaxException {
        displayId = id;
        if (field == null)
            newGame();
        fields.put(id, field);
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/display/unpair")
    public String unpair(Model model) {
        fields.remove(displayId);
        displayId = 0;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }



    public String getHtmlField(Field field) throws IOException {
        StringBuilder sb = new StringBuilder();
        if(field!=null) {
                if (field != null) {
                    sb.append("<div class = container4>");
                    sb.append("<table class = 'field'>\n");
                    for (int row = 0; row < field.getRowCount(); row++) {
                        sb.append("<tr>\n");
                        for (int col = 0; col < field.getColumnCount(); col++) {
                            sb.append("<td>\n");
                            if(puzzles[row][col]!=null&& field.equals(this.field)) {
                                sb.append("<a href='" + String.format("%s/pictureslidingpuzzle-paluch?row=%s&col=%s",servletContext.getContextPath(),row, col)
                                        + "'>\n");
                                //sb.append("<img src='%s/images/pictureslidingpuzzle/paluch/" + puzzles[row][col].imgValue() + ".png' height='" + 500 / field.getRowCount() + "' width= '" + 500 / field.getColumnCount() + "'>");
                                sb.append(puzzles[row][col].encode()+">");
                                sb.append("</a>\n");
                            }
                            else{
                                if(!field.isSolved()) {
                                    sb.append("<img src='https://www.halberesford.com/content/images/2018/07/null.png' height='" + 500 / field.getRowCount() + "' width= '" + 500 / field.getColumnCount() + "'>");
                                }
                                else {
                                    sb.append(field.getNullPuzzle().encode()+">");
                                }
                            }
                            sb.append("</td>\n");
                        }
                        sb.append("</tr>\n");
                    }
                    sb.append("</table>\n");
                    sb.append("</div>");
                    return sb.toString();
                }
            } else if (!diffChosen) {
                sb.append("Please choose a difficulty to start the game my mastah");
                return sb.toString();
            }
        return "pictureslidingpuzzle-paluch";
    }
}
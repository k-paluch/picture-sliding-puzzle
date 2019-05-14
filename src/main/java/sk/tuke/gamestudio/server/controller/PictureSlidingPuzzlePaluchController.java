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
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.*;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/pictureslidingpuzzle-paluch")
public class PictureSlidingPuzzlePaluchController {
    private static Map<Integer, Field> fields = new HashMap<>();
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    @Autowired
    ServletContext servletContext;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ScoreService scoreService;
    private String URL = "http://www.best.tuke.sk/local/images/partners/kpi.png";
    private Field field ;
    private Puzzle[][] puzzles ;
    private Difficulty difficulty = Difficulty.EASY;
    private boolean diffChosen = false;
    private String loggedUser;
    private boolean addedToScore = false;
    private int displayId =0 ;



    @RequestMapping
    public String pictureslidingpuzzle(String row, String col, Model model) throws CommentException {
        if(field!=null) {
            if(!field.isSolved()) {
                try {
                    field.moveWeb(row + col);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if(field.isSolved() && !addedToScore){
                addScore(field.getScore(),model);
            }
        }
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }


    public String getLoggedUser(){
        return loggedUser;
    }


    @RequestMapping("/field")
    public String field(String row, String col,Model model) throws CommentException {
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

    public int averageRating() throws RatingException {
        return this.ratingService.getAverageRating("pictureslidingpuzzle");
    }

    public Difficulty getDifficulty() {
        return field!=null ? field.getDifficulty(): null;
    }

    @RequestMapping("/new")
    public String newGame(Model model) throws IOException, URISyntaxException, CommentException {
        field = null;
        difficulty=null;
        fields.remove(displayId);
        updateModel(model);
        addedToScore = false;
        return "pictureslidingpuzzle-paluch";
    }


    @RequestMapping("/changePic")
    public String changePic(String URL, Model model) throws IOException, URISyntaxException, CommentException {
        Matcher matcher = urlPattern.matcher(URL);
        if(matcher.matches()) {
            if (URL.substring(URL.lastIndexOf(".")).equals(".png")) {
                this.URL = URL;
                newGame();
                updateModel(model);
            }
        }
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/addComment")
    public String addComment(String comment, Model model) throws CommentException {
        if(loggedUser == null){
            return "pictureslidingpuzzle-paluch";
        }
        Date date = new Date();
        Comment c = new Comment(loggedUser,"pictureslidingpuzzle",comment,date);
        commentService.addComment(c);
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }
    @RequestMapping("/addScore")
    public String addScore(int score, Model model) throws CommentException {
        if(loggedUser == null){
            return "pictureslidingpuzzle-paluch";
        }
        Date date = new Date();
        Score s = new Score("pictureslidingpuzzle",loggedUser,score,date);
        scoreService.addScore(s);
        addedToScore = true;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }


    @RequestMapping("/setRating")
    public String setRating(String rating, Model model) throws CommentException {
        if(loggedUser == null){
            return "pictureslidingpuzzle-paluch";
        }
        Rating r = new Rating(loggedUser,"pictureslidingpuzzle",Integer.parseInt(rating),new java.util.Date());
        try {
            ratingService.setRating(r);
        }catch (RatingException e){
            e.printStackTrace();
        }
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/easy")
    public String easyD(Model model) throws IOException, URISyntaxException, CommentException {
        field = new Field(3, 3,URL);
        difficulty = Difficulty.EASY;
        field.setDifficulty(difficulty);
        field.shuffle(5);
        puzzles = field.getPuzzles();
        diffChosen = true;
        if(displayId!=0) {
            fields.put(displayId, field);
        }
        addedToScore = false;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/medium")
    public String mediumD(Model model) throws IOException, URISyntaxException, CommentException {
        field = new Field(4, 4,URL);
        difficulty = Difficulty.MEDIUM;
        field.setDifficulty(difficulty);
        field.shuffle(50);
        puzzles = field.getPuzzles();
        diffChosen = true;
        if(displayId!=0) {
            fields.put(displayId, field);
        }
        addedToScore = false;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/hard")
    public String hard(Model model) throws IOException, URISyntaxException, CommentException {
        field = new Field(5, 5,URL);
        difficulty = Difficulty.HARD;
        field.setDifficulty(difficulty);
        field.shuffle(100);
        puzzles = field.getPuzzles();
        diffChosen = true;
        if(displayId!=0) {
            fields.put(displayId, field);
        }
        addedToScore = false;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/login")
    public String login(String login, Model model) throws CommentException {
        loggedUser = login;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/logout")
    public String logout(Model model) throws CommentException {
        loggedUser = null;
        updateModel(model);
        return "pictureslidingpuzzle-paluch_login";
    }

    private void newGame() throws IOException, URISyntaxException {
        fields.remove(displayId);
        field = null;
        difficulty=null;
        addedToScore = false;
    }

    @RequestMapping("/loginview")
    public String log(Model model) throws CommentException {
        updateModel(model);
        return "pictureslidingpuzzle-paluch_login";
    }

    @RequestMapping("/display/{id}")
    public String display(@PathVariable int id, Model model) throws IOException, CommentException {
        displayId = id;
        Field field =  fields.get(id);
        if (field != null)
            model.addAttribute("htmlField", getHtmlField(field));

        if(field!=null)
        puzzles = field.getPuzzles();
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

    public List<Comment> getComments () throws CommentException {
        return commentService.getComments("pictureslidingpuzzle");
    }

    private void updateModel(Model model) throws CommentException {
        model.addAttribute("displayId", displayId);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("scores", scoreService.getBestScores("pictureslidingpuzzle"));
        model.addAttribute("comments", commentService.getComments("pictureslidingpuzzle"));
    }
    public String getHtmlField() throws IOException {
        return getHtmlField(field);
    }


    @RequestMapping("/display/pair/{id}")
    public String pair(@PathVariable int id, Model model) throws IOException, URISyntaxException, CommentException {
        displayId = id;
        if (field == null)
            newGame();
        fields.put(id, field);
        if(field!=null) {
            puzzles = field.getPuzzles();
        }
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    @RequestMapping("/display/unpair")
    public String pair(Model model) throws CommentException {
        fields.remove(displayId);
        displayId = 0;
        updateModel(model);
        return "pictureslidingpuzzle-paluch";
    }

    public String getHtmlAverageRating() throws RatingException {
        StringBuilder sb = new StringBuilder();
            if (Math.round(averageRating()) == 1) {
                sb.append("<div class='rating'>\n" +
                        "        <input type='radio' name='star' value='1' id='star1'/><label for='star1' ></label>\n" +
                        "        <input type='radio' name='star' value='2'id='star2'/><label for='star2' ></label>\n" +
                        "        <input type='radio' name='star' value='3'id='star3'/><label for='star3'></label>\n" +
                        "        <input type='radio' name='star' value='4'id='star4'/><label for='star4'></label>\n" +
                        "        <input type='radio' name='star' value='5'id='star5'checked/><label for='star5'></label>\n" +
                        "    </div>");
            } else if (Math.round(averageRating()) == 2) {
                sb.append("<div class='rating'>\n" +
                        "        <input type='radio' name='star' value='1' id='star1' /><label for='star1' ></label>\n" +
                        "        <input type='radio' name='star' value='2'id='star2'/><label for='star2' ></label>\n" +
                        "        <input type='radio' name='star' value='3'id='star3'/><label for='star3'></label>\n" +
                        "        <input type='radio' name='star' value='4'id='star4'checked/><label for='star4'></label>\n" +
                        "        <input type='radio' name='star' value='5'id='star5'/><label for='star5'></label>\n" +
                        "    </div>");

            } else if (Math.round(averageRating()) == 3) {
                sb.append("<div class='rating'>\n" +
                        "        <input type='radio' name='star' value='1' id='star1'/><label for='star1' ></label>\n" +
                        "        <input type='radio' name='star' value='2'id='star2'/><label for='star2' ></label>\n" +
                        "        <input type='radio' name='star' value='3'id='star3'checked/><label for='star3'></label>\n" +
                        "        <input type='radio' name='star' value='4'id='star4'/><label for='star4'></label>\n" +
                        "        <input type='radio' name='star' value='5'id='star5'/><label for='star5'></label>\n" +
                        "    </div>");

            } else if (Math.round(averageRating()) == 4) {
                sb.append("<div class='rating'>\n" +
                        "        <input type='radio' name='star' value='1' id='star1'/><label for='star1' ></label>\n" +
                        "        <input type='radio' name='star' value='2'id='star2'checked/><label for='star2' ></label>\n" +
                        "        <input type='radio' name='star' value='3'id='star3'/><label for='star3'></label>\n" +
                        "        <input type='radio' name='star' value='4'id='star4'/><label for='star4'></label>\n" +
                        "        <input type='radio' name='star' value='5'id='star5'/><label for='star5'></label>\n" +
                        "    </div>");

            } else if (Math.round(averageRating()) == 5) {
                sb.append("<div class='rating'>\n" +
                        "        <input type='radio' name='star' value='1' id='star1'checked/><label for='star1' ></label>\n" +
                        "        <input type='radio' name='star' value='2'id='star2'/><label for='star2' ></label>\n" +
                        "        <input type='radio' name='star' value='3'id='star3'/><label for='star3'></label>\n" +
                        "        <input type='radio' name='star' value='4'id='star4'/><label for='star4'></label>\n" +
                        "        <input type='radio' name='star' value='5'id='star5'/><label for='star5'></label>\n" +
                        "    </div>");

            }
        else sb.append("<div class='rating'>\n" +
                "        <input type='radio' name='star' value='1' id='star1'/><label for='star1' ></label>\n" +
                "        <input type='radio' name='star' value='2'id='star2'/><label for='star2' ></label>\n" +
                "        <input type='radio' name='star' value='3'id='star3'/><label for='star3'></label>\n" +
                "        <input type='radio' name='star' value='4'id='star4'/><label for='star4'></label>\n" +
                "        <input type='radio' name='star' value='5'id='star5'/><label for='star1'></label>\n" +
                "    </div>");
        return sb.toString();
    }



    public String getHtmlField(Field field) throws IOException {
        StringBuilder sb = new StringBuilder();
        if(field!=null) {
                if (field != null) {
                    sb.append("<div class = puzzles>");
                    sb.append("<table align='center' cellspacing='0' cellpadding='0' class = 'field'>\n");
                    for (int row = 0; row < field.getRowCount(); row++) {
                        sb.append("<tr>\n");
                        for (int col = 0; col < field.getColumnCount(); col++) {
                            sb.append("<td>\n");
                            puzzles = field.getPuzzles();
                            if(puzzles[row][col]!=null){
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
                sb.append(" ");
                return sb.toString();
            }
        return "pictureslidingpuzzle-paluch";
    }
}
package sk.tuke.gamestudio.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.text.SimpleDateFormat;
import java.util.Date;
@Entity
@NamedQuery( name = "Comment.getComments",
        query = "SELECT c FROM Comment c WHERE c.game=:game ORDER BY c.commentedOn DESC")
public class Comment {
    private String player;
    private String game;
    private String comment;
    private Date commentedOn;
    @Id
    @GeneratedValue
    private int ident;
    public Comment() {}
    public int getIdent() { return ident; }
    public void setIdent(int ident) { this.ident = ident; }
    public Comment(String player, String game, String comment, Date commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(getCommentedOn().getTime())+
                ' ' + player + ": " +
                "\033[0;1m" +comment;
    }
}

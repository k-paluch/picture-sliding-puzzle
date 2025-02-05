package sk.tuke.gamestudio.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
@Entity
@NamedQuery( name = "Score.getBestScores",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC")
public class Score implements Comparable<Score>, Serializable {
    private String game;

    private String player;

    private int points;

    private Date playedOn;
    @Id
    @GeneratedValue
    private int ident;
    public Score() {}

    public Score(String game, String player, int points, Date playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }
    public int getIdent() { return ident; }
    public void setIdent(int ident) { this.ident = ident; }

    @Override
    public String toString() {
        return  points +
                "\t\t " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(playedOn.getTime()) + "\t" +player ;
    }

    @Override
    public int compareTo(Score o) {
        if(o == null) return -1;
        return this.getPoints() - o.getPoints();
    }
}

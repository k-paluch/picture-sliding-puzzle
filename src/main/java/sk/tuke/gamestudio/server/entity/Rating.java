package sk.tuke.gamestudio.server.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@NamedQueries({
        @NamedQuery(name = "Rating.getRating",
                query = "SELECT r FROM Rating r WHERE r.game=:game"),
        @NamedQuery(name = "Rating.getAverageRating",
                query = "SELECT AVG (rating) FROM Rating WHERE game=:game")
})
public class Rating {
    private String player;
    private String game;
    private int rating;
    private Date ratedon;
    @Id
    @GeneratedValue
    private int ident;
    public Rating() {}
    public int getIdent(){
        return this.ident;
    }
    public void setIdent(int ident){
        this.ident = ident;
    }
    public Rating(String player, String game, int rating, Date ratedon) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedon = ratedon;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedon() {
        return ratedon;
    }

    public void setRatedon(Date ratedon) {
        this.ratedon = ratedon;
    }

    @Override
    public String toString() {
        return "Rating{" + "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", rating=" + rating +
                ", ratedon=" + ratedon +
                '}';
    }
}

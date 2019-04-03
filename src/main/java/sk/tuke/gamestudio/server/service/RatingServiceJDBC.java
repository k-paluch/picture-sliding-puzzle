package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";


    private final String SELECT_RATING =
            "SELECT game, player, rating, ratedon FROM rating WHERE (game = ?)";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String INSERT_RATING = "INSERT INTO rating (player, game, rating, ratedon) " +
                    "VALUES (?, ?, ?,?) ON CONFLICT (player) DO UPDATE SET rating = ?";
            try (PreparedStatement ps = connection.prepareStatement(INSERT_RATING)) {
                ps.setString(1, rating.getPlayer());
                ps.setString(2, rating.getGame());
                ps.setInt(3, rating.getRating());
                ps.setTimestamp(4, new Timestamp(rating.getRatedon().getTime()));
                ps.setInt(5, rating.getRating());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Error saving rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SELECT_RATING)) {
                ps.setString(1, game);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        return rs.getInt(3);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
        return 0;
    }


    @Override
    public int getAverageRating(String game) throws RatingException {
        float sum = 0;
        int size = 0;
        try(Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){
            try(PreparedStatement ps = connection.prepareStatement(SELECT_RATING)){
                ps.setString(1,game);
                try (ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        sum+=rs.getInt(3);
                        size++;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating",e);
        }
        if(size == 0){
            return 0;
        }
        return (int)sum/size;
    }

}

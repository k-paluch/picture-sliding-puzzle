package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Score;

import java.sql.SQLException;
import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException, SQLException;
    List<Score> getBestScores(String game) throws ScoreException;
}

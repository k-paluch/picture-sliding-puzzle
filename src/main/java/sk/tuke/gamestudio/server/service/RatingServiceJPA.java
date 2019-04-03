package sk.tuke.gamestudio.server.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.server.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Float temp = Float.valueOf(entityManager.createNamedQuery("Rating.getAverageRating").setParameter("game",game).getSingleResult().toString());
        return temp.intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return (int)entityManager.createNamedQuery("Rating.getRating").setParameter("game",game).getSingleResult();
    }
}
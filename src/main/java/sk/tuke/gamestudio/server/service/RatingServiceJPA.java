package sk.tuke.gamestudio.server.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.server.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        List<Rating> ratings = entityManager.createNamedQuery("Rating.setRating").setParameter("player",rating.getPlayer()).getResultList();
        if(!ratings.isEmpty()){
            ratings.get(0).setRating(rating.getRating());
            entityManager.merge(ratings.get(0));
        }else
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Float v = Float.valueOf(entityManager.createNamedQuery("Rating.getAverageRating").setParameter("game",game).getSingleResult().toString());
        return v.intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Rating r = (Rating) entityManager.createNamedQuery("Rating.getRating").setParameter("game",game).setParameter("player",player).getSingleResult();
        return r.getRating();
    }
}
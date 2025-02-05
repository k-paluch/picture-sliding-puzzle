package sk.tuke.gamestudio.server.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.server.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return entityManager.createNamedQuery("Comment.getComments")
                .setParameter("game", game).getResultList();
    }
}
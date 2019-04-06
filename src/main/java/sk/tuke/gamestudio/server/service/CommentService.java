package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List getComments(String game) throws CommentException;
}
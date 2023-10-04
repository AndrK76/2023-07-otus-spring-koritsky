package ru.otus.andrk.repository;

import ru.otus.andrk.model.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    Comment insertComment(Comment comment);

    List<Comment> findCommentsByBookId(long bookId);
}

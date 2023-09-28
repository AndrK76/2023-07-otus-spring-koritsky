package ru.otus.andrk.repository;

import ru.otus.andrk.model.Comment;

public interface CommentRepositoryCustom {
    Comment insertComment(Comment comment);
}

package ru.otus.andrk.repository;

import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findCommentsForBook(Book book);

    Optional<Comment> findCommentById(long id);

    Comment save(Comment comment);

    void delete(Comment comment);

}

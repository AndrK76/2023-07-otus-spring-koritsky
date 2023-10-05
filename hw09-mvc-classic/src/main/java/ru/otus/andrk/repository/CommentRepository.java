package ru.otus.andrk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByBook(Book book);
}

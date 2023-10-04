package ru.otus.andrk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, Long>, CommentRepositoryCustom {
    List<Comment> findCommentsByBook(Book book);
}

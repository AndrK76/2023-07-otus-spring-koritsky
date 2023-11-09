package ru.otus.andrk.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.andrk.model.Comment;

public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByBookId(String bookId);


}

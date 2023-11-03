package ru.otus.andrk.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.model.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, String> {
    Mono<Author> findFirstByName(String name);
}

package ru.otus.andrk.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.andrk.model.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, String> {

    Mono<Genre> findFirstByName(String name);
}

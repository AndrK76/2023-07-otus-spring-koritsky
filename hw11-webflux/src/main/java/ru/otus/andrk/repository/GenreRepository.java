package ru.otus.andrk.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.andrk.model.Genre;

public interface GenreRepository extends ReactiveCrudRepository<Genre, String> {
}

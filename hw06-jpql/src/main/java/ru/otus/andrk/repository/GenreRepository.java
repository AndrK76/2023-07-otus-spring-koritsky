package ru.otus.andrk.repository;

import ru.otus.andrk.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Genre save(Genre genre);

    List<Genre> findAll();

    Optional<Genre> findById(long id);
}

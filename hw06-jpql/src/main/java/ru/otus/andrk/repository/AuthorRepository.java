package ru.otus.andrk.repository;

import ru.otus.andrk.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);

    List<Author> findAll();

    Optional<Author> findById(long id);
}

package ru.otus.andrk.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.andrk.model.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, String> {
}

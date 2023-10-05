package ru.otus.andrk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.andrk.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}

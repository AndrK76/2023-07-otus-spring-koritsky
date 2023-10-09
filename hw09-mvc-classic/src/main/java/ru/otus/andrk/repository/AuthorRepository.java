package ru.otus.andrk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.andrk.model.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAuthorsByNameIgnoreCase(String name);

}

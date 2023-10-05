package ru.otus.andrk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.andrk.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}

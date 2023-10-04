package ru.otus.andrk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.andrk.model.Genre;

public interface GenreRepository extends MongoRepository<Genre, Long>, GenreRepositoryCustom {
}

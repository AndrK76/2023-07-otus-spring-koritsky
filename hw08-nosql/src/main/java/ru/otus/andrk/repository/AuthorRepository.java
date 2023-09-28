package ru.otus.andrk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.andrk.model.Author;

public interface AuthorRepository extends MongoRepository<Author, Long>, AuthorRepositoryCustom {
}

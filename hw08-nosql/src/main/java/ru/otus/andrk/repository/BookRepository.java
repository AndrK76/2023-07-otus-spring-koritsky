package ru.otus.andrk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.andrk.model.Book;

public interface BookRepository extends MongoRepository<Book, Long>, BookRepositoryCustom {
}

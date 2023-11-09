package ru.otus.andrk.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.otus.andrk.model.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, String> {
}

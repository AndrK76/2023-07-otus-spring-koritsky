package ru.otus.andrk.repository;

import ru.otus.andrk.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(long id);

    void delete(Book book);
}

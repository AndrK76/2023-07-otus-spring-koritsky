package ru.otus.andrk.dao;

import ru.otus.andrk.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> getAll();

    Book getById(long id);

    void insert(Book book);

    void update(Book book);

    void delete(long id);
}

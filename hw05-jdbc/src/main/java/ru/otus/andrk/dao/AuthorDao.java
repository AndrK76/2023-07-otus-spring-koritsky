package ru.otus.andrk.dao;

import ru.otus.andrk.model.Author;

import java.util.List;

public interface AuthorDao {
    List<Author> getAll();

    Author getById(long id);

    long insert(Author author);
}

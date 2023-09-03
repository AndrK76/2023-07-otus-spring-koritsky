package ru.otus.andrk.dao;

import ru.otus.andrk.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getAll();

    Genre getById(long id);

    void insert(Genre genre);

}

package ru.otus.andrk.service;

import ru.otus.andrk.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();

    Genre addGenre(String genreName);

    Genre getGenreById(long genreId);
}

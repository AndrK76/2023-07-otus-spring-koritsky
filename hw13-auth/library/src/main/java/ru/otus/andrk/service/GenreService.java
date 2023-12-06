package ru.otus.andrk.service;

import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<GenreDto> getAllGenres();

    Genre addGenre(String genreName);

    Optional<Genre> getGenreByName(String name);
}

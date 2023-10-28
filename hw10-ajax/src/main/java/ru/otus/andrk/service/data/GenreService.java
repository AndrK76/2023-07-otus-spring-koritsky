package ru.otus.andrk.service.data;

import ru.otus.andrk.dto.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> getAllGenres();

    GenreDto addGenre(String genreName);

    GenreDto getGenreById(long genreId);

    GenreDto getGenreByName(String name);
}

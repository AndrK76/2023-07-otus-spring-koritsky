package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dao.GenreDao;
import ru.otus.andrk.exception.NoExistGenreException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class GenreServiceImpl implements GenreService {
    private final GenreDao dao;

    @Override
    public List<Genre> getAllGenres() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Genre addGenre(String genreName) {
        try {
            var genre = new Genre(0L, genreName);
            var newId = dao.insert(genre);
            return dao.getById(newId);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Genre getGenreById(long genreId) {
        Genre genre;
        try {
            genre = dao.getById(genreId);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
        if (genre == null) {
            throw new NoExistGenreException();
        }
        return genre;
    }
}

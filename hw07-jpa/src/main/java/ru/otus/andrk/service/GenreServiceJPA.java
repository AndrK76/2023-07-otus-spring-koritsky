package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.excepton.OtherLibraryManipulationException;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class GenreServiceJPA implements GenreService {
    private final GenreRepository repo;

    @Override
    public List<Genre> getAllGenres() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional
    public Genre addGenre(String genreName) {
        try {
            return repo.save(new Genre(0, genreName));
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Genre getGenreById(long genreId) {
        try {
            return repo.findById(genreId).orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }
}

package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repo;

    private final DtoMapper mapper;

    @Override
    public List<GenreDto> getAllGenres() {
        try {
            return repo.findAll().stream().map(mapper::toDto).toList();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional
    public GenreDto addGenre(String genreName) {
        try {
            return mapper.toDto(
                    repo.save(new Genre(0, genreName)));
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public GenreDto getGenreById(long genreId) {
        try {
            return repo.findById(genreId).map(mapper::toDto).orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }
}

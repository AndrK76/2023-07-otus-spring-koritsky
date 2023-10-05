package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository repo;

    private final DtoMapper mapper;


    @Override
    public List<AuthorDto> getAllAuthors() {
        try {
            return repo.findAll().stream().map(mapper::toDto).toList();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional
    public AuthorDto addAuthor(String authorName) {
        try {
            return mapper.toDto(
                    repo.save(new Author(0, authorName)));
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public AuthorDto getAuthorById(long authorId) {
        try {
            return repo.findById(authorId).map(mapper::toDto).orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }
}

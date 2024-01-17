package ru.otus.andrk.service.data;

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
import java.util.Optional;

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
    public Author addAuthor(String authorName) {
        try {
            return repo.save(new Author(0, authorName));
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Optional<Author> getAuthorByName(String name) {
        return repo.findAuthorsByNameIgnoreCase(name).stream().findFirst();
    }
}

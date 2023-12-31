package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.excepton.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.repository.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repo;

    @Override
    public List<Author> getAllAuthors() {
        try {
            return repo.findAll();
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
    public Author getAuthorById(long authorId) {
        try {
            return repo.findById(authorId).orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }
}

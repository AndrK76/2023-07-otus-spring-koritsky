package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dao.AuthorDao;
import ru.otus.andrk.exception.NoExistAuthorException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao dao;

    @Override
    public List<Author> getAllAuthors() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Author addAuthor(String authorName) {
        try {
            var author = new Author(0L, authorName);
            var newId = dao.insert(author);
            return dao.getById(newId);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Author getAuthorById(long authorId) {
        Author author;
        try {
            author = dao.getById(authorId);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
        if (author == null) {
            throw new NoExistAuthorException();
        }
        return author;
    }
}

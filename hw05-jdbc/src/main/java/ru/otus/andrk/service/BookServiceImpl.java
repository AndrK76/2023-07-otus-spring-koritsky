package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dao.BookDao;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookServiceImpl implements BookService {

    private final BookDao dao;

    @Override
    public List<Book> getAllBooks() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Book getBookById(long id) {
        try {
            return dao.getById(id);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }

    }

    @Override
    public Book addBook(String bookName, Author author, Genre genre) {
        try {
            var book = new Book(0L, bookName, author, genre);
            var newId = dao.insert(book);
            return dao.getById(newId);
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public Book modifyBook(long bookId, String newName, Author newAuthor, Genre newGenre) {
        try {
            var book = new Book(bookId, newName, newAuthor, newGenre);
            dao.update(book);
            return dao.getById(bookId);
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public void deleteBook(long id) {
        try {
            dao.delete(id);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

}

package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dao.AlreadyExistObjectException;
import ru.otus.andrk.dao.AuthorDao;
import ru.otus.andrk.dao.BookDao;
import ru.otus.andrk.dao.GenreDao;
import ru.otus.andrk.dao.NoExistObjectException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class LibraryServiceDao implements LibraryService {

    private final BookDao bookDao;

    private final GenreDao genreDao;

    private final AuthorDao authorDao;

    @Override
    public List<Book> getAllBooks() {
        try {
            return bookDao.getAll();
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }
    }

    @Override
    public Book getBookById(long id) {
        try {
            return bookDao.getById(id);
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }

    }

    @Override
    public Book addBook(Book book) {
        try {
            fillIfExistAuthorAndGenreValuesFromLibrary(book);
            bookDao.insert(book);
            return book;
        } catch (AlreadyExistObjectException e) {
            log.error(e);
            throw new AddAlreadyExistBookException();
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }
    }

    @Override
    public Book modifyBook(Book book) {
        try {
            fillIfExistAuthorAndGenreValuesFromLibrary(book);
            bookDao.update(book);
            return book;
        } catch (NoExistObjectException e) {
            log.error(e);
            throw new ModifyNoExistBookException();
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }
    }

    @Override
    public void deleteBook(long id) {
        try {
            bookDao.delete(id);
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        try {
            return authorDao.getAll();
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        try {
            return genreDao.getAll();
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }
    }

    private void fillIfExistAuthorAndGenreValuesFromLibrary(Book book) {
        if (book.getAuthor() != null) {
            book.setAuthor(getAuthorByIdAndAddToLibraryWhenNoExist(book.getAuthor()));
        }
        if (book.getGenre() != null) {
            book.setGenre(getGenreByIdAndAddToLibraryWhenNoExist(book.getGenre()));
        }
    }

    private Author getAuthorByIdAndAddToLibraryWhenNoExist(Author author) {
        Author ret = authorDao.getById(author.id());
        if (ret != null) {
            return ret;
        } else {
            authorDao.insert(author);
            return author;
        }
    }

    private Genre getGenreByIdAndAddToLibraryWhenNoExist(Genre genre) {
        Genre ret = genreDao.getById(genre.id());
        if (ret != null) {
            return ret;
        } else {
            genreDao.insert(genre);
            return genre;
        }
    }

}

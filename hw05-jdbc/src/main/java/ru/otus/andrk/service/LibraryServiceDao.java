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
    public LibraryManipulationResult getAllBooks() {
        try{
            var data = bookDao.getAll();
            return new LibraryManipulationResult(true,data, null);
        } catch (Exception e){
            log.error(e);
            return makeFailureResult("Can't get all book, see log for details");
        }
    }

    @Override
    public LibraryManipulationResult getBookById(long id) {
        try{
            var data = bookDao.getById(id);
            return new LibraryManipulationResult(true,data, null);
        } catch (Exception e){
            log.error(e);
            return makeFailureResult("Can't get book, see log for details");
        }

    }

    @Override
    public LibraryManipulationResult addBook(Book book) {
        try {
            fillIfExistAuthorAndGenreValuesFromLibrary(book);
            bookDao.insert(book);
            return new LibraryManipulationResult(true,bookDao.getById(book.getId()),"Book added to library");
        } catch (AlreadyExistObjectException e) {
            log.error(e);
            return makeFailureResult("Book with id=" + book.getId() + " already exist in library");
        } catch (Exception e) {
            log.error(e);
            return makeFailureResult("Can't add book to library, see log for detail");
        }
    }

    @Override
    public void modifyBook(Book book) {
        try {
            fillIfExistAuthorAndGenreValuesFromLibrary(book);
            bookDao.update(book);
        } catch (NoExistObjectException e) {
            log.error(e);
            throw new ModifyNoExistBookException();
        } catch (Exception e) {
            log.error(e);
            throw new OtherBookManipulationException();
        }
    }

    private LibraryManipulationResult makeFailureResult(String message){
        return new LibraryManipulationResult(false,null,message);
    }




    private void fillIfExistAuthorAndGenreValuesFromLibrary(Book book){
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

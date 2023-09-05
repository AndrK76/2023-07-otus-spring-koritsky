package ru.otus.andrk.service;

import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.util.List;

public interface LibraryService {
    List<Book> getAllBooks();

    Book getBookById(long id);

    Book addBook(Book book);

    Book modifyBook(Book book);

    void deleteBook(long id);

    List<Author> getAllAuthors();

    List<Genre> getAllGenres();
}

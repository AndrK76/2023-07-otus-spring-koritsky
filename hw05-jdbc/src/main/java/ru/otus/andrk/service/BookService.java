package ru.otus.andrk.service;

import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book getBookById(long id);

    Book addBook(String bookName, Author author, Genre genre);

    Book modifyBook(long bookId, String newName, Author newAuthor, Genre newGenre);

    void deleteBook(long id);

}

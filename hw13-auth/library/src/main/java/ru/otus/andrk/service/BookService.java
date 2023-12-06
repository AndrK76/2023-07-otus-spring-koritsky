package ru.otus.andrk.service;

import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookDto> getAllBooks();

    Optional<Book> getBookById(long id);

    BookDto addBook(BookDto book);

    BookDto modifyBook(long bookId, BookDto book);

    void deleteBook(long id);

}

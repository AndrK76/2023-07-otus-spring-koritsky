package ru.otus.andrk.service.data;

import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.model.Book;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    BookDto getBookById(long id);

    BookWithCommentsDto getBookWithCommentsById(long id);

    BookDto addBook(BookDto book);

    BookDto modifyBook(long bookId, BookDto book);

    void deleteBook(long id);

}

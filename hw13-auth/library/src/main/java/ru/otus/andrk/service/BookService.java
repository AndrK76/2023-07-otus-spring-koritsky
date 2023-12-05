package ru.otus.andrk.service;

import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    BookDto getBookById(long id);

    BookDto addBook(String bookName, Long authorId, Long genreId);

    BookDto modifyBook(long bookId, String newName, Long newAuthorId, Long newGenreId);

    void deleteBook(long id);

}

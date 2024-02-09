package ru.otus.andrk.service.library;

import org.springframework.http.ResponseEntity;
import ru.otus.andrk.dto.BookDto;

public interface BookService {
    ResponseEntity<?> getAllBooks();

    ResponseEntity<?> addBook(BookDto book);

    ResponseEntity<?> modifyBook(long bookId, BookDto book);

    ResponseEntity<?> deleteBook(long bookId);
}

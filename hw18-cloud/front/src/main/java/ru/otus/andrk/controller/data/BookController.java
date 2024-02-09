package ru.otus.andrk.controller.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.service.library.BookService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/api/v1/book")
    public ResponseEntity<?> addBook(@RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @PutMapping("/api/v1/book/{book}")
    public ResponseEntity<?> modifyBook(@PathVariable(name = "book") long bookId,
                                        @RequestBody BookDto book) {
        return bookService.modifyBook(bookId, book);
    }

    @DeleteMapping("/api/v1/book/{book}")
    public ResponseEntity<?> deleteBook(@PathVariable(name = "book") long bookId) {
        return bookService.deleteBook(bookId);
    }

}

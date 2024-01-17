package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.service.data.BookService;
import ru.otus.andrk.service.health.HealthService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin("*")
public class BookController {

    private final BookService bookService;

    private final HealthService healthService;

    @GetMapping("/api/v1/book")
    public List<BookDto> getAllBooks() {
        healthService.registerVisitor();
        return bookService.getAllBooks();
    }

    @PostMapping("/api/v1/book")
    public BookDto addBook(@RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @PutMapping("/api/v1/book/{book}")
    public BookDto modifyBook(@PathVariable(name = "book") long bookId,
                              @RequestBody BookDto book) {
        return bookService.modifyBook(bookId, book);
    }

    @DeleteMapping("/api/v1/book/{book}")
    public long deleteBook(@PathVariable(name = "book") long bookId) {
        bookService.deleteBook(bookId);
        return bookId;
    }

}

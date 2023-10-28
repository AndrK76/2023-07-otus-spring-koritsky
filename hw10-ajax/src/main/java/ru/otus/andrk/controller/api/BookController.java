package ru.otus.andrk.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.BookService;
import ru.otus.andrk.service.data.GenreService;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/api/v1/book")
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/api/v1/book/{id}")
    public BookDto getBook(@PathVariable(name = "id") long bookId) {
        return bookService.getBookById(bookId);
    }

    @GetMapping("/api/v1/book/{id}/comments")
    public BookWithCommentsDto getBookWithComments(@PathVariable(name = "id") long bookId) {
        return bookService.getBookWithCommentsById(bookId);
    }

    @PostMapping("/api/v1/book")
    public BookDto addBook(@Valid @RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @PutMapping("/api/v1/book/{id}")
    public BookDto modifyBook(
            @PathVariable(name = "id") long bookId,
            @RequestBody @Valid BookDto book) {
        return bookService.modifyBook(bookId, book);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public void deleteBook(@PathVariable(name = "id") long bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping("/api/v1/author")
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/api/v1/genre")
    public List<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

}

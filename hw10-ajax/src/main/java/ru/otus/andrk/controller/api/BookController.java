package ru.otus.andrk.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.BookService;
import ru.otus.andrk.service.data.GenreService;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/api/v1/book")
    public BookDto addBook(@Valid @RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @GetMapping("/api/v1/book/{id}")
    public BookDto getBook(@PathVariable(name = "id") long bookId) {
        return bookService.getBookById(bookId);
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

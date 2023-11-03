package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.BookService;
import ru.otus.andrk.service.data.GenreService;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    private final GenreService genreService;

    private final LocaleContextResolver localeResolver;


    @GetMapping("/api/v1/book")
    public Flux<BookDto> getAllBooks() {
        return bookService.getAllBooks()
                .onErrorMap(OtherLibraryManipulationException::new);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public Mono<Void> deleteBookBiId(@PathVariable(name = "id") String bookId) {
        return bookService.deleteBook(bookId);
    }

    @PostMapping("/api/v1/book")
    public Mono<BookDto> addBook(@RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @PutMapping("/api/v1/book/{id}")
    public Mono<BookDto> modifyBook(
            @PathVariable(name = "id") String bookId,
            @RequestBody BookDto book) {
        return bookService.modifyBook(bookId, book);
    }


    @GetMapping("/api/v1/author")
    public Flux<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/api/v1/genre")
    public Flux<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }


}

package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.BookService;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;


    @GetMapping("/api/v1/book")
    public Flux<BookDto> getAllBooks(){
        return bookService.getAllBooks();
    }


    @GetMapping("/api/v1/author")
    public Flux<AuthorDto> getAllAuthors(){
        return authorService.getAllAuthors();
    }


}

package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.service.main.BookService;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/book")
    public List<BookDto> getAllBooks(){
        return bookService.getAllBooks();
    }

}

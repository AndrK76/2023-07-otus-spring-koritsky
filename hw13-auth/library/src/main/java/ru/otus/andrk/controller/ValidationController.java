package ru.otus.andrk.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;

@RestController
public class ValidationController {

    @PostMapping(value = "/api/v1/validation/book")
    public BookDto validateBook(@Valid @RequestBody BookDto book) {
        return book;
    }

    @PostMapping(value = "/api/v1/validation/comment")
    public CommentDto validateComment(@Valid @RequestBody CommentDto comment) {
        return comment;
    }
}

package ru.otus.andrk.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ValidationController {

    @PostMapping(value = "/api/v1/validation/book")
    public Mono<BookDto> validateBook(
            @RequestBody @Valid BookDto book) {
        return Mono.just(book);
    }

    @PostMapping(value = "/api/v1/validation/comment")
    public Mono<CommentDto> validateComment(
            @RequestBody @Valid CommentDto comment) {
        return Mono.just(comment);
    }
}

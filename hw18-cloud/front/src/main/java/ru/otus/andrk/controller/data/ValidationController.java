package ru.otus.andrk.controller.data;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.service.library.ValidationService;

@RestController
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @PostMapping(value = "/api/v1/validation/book")
    public ResponseEntity<?> validateBook(@RequestBody BookDto book) {
        return validationService.validateBook(book);
    }

    @PostMapping(value = "/api/v1/validation/comment")
    public ResponseEntity<?> validateComment(@RequestBody CommentDto comment) {
        return validationService.validateComment(comment);
    }
}

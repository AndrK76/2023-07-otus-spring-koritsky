package ru.otus.andrk.service.library;

import org.springframework.http.ResponseEntity;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;

public interface ValidationService {
    ResponseEntity<?> validateBook(BookDto book);

    ResponseEntity<?> validateComment(CommentDto comment);
}

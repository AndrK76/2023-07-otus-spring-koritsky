package ru.otus.andrk.service.library;

import org.springframework.http.ResponseEntity;
import ru.otus.andrk.dto.CommentDto;

public interface CommentService {
    ResponseEntity<?> getCommentsForBook(long bookId);

    ResponseEntity<?> addComment(long bookId, CommentDto comment);

    ResponseEntity<?> modifyComment(long commentId, CommentDto comment);

    ResponseEntity<?> deleteComment(long commentId);
}

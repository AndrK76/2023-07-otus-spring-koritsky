package ru.otus.andrk.controller.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.service.library.CommentService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/book/{book}/comment")
    public ResponseEntity<?> getCommentsForBook(@PathVariable(name = "book") long bookId) {
        return commentService.getCommentsForBook(bookId);
    }

    @PostMapping("/api/v1/book/{book}/comment")
    public ResponseEntity<?> addComment(@PathVariable(name = "book") long bookId,
                                        @RequestBody CommentDto comment) {
        return commentService.addComment(bookId, comment);
    }

    @PutMapping("/api/v1/comment/{comment}")
    public ResponseEntity<?> modifyComment(@PathVariable(name = "comment") long commentId,
                                           @RequestBody CommentDto comment) {
        return commentService.modifyComment(commentId, comment);
    }

    @DeleteMapping("/api/v1/comment/{comment}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "comment") long commentId) {
        return commentService.deleteComment(commentId);
    }
}

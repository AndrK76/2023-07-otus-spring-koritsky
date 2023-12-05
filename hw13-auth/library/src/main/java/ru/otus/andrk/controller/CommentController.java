package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/v1/book/{book}/comment")
    public List<CommentDto> getCommentsForBook(@PathVariable(name = "book") long bookId){
        return commentService.getCommentsForBook(bookId);
    }
}

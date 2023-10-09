package ru.otus.andrk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistCommentException;
import ru.otus.andrk.service.BookService;
import ru.otus.andrk.service.CommentService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private static final String ACTION_ADD = "add";
    private static final String ACTION_EDIT = "edit";

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping({"/book/{book}/comment"})
    public String getCommentsForBook(
            @PathVariable(name = "book") long bookId, Model model) {
        var book = Optional.ofNullable(bookService.getBookWithCommentsById(bookId))
                .orElseThrow(NoExistBookException::new);
        model.addAttribute("book", book);
        return "comments";
    }

    @GetMapping("/book/{book}/comment/add")
    public String addComment(
            @PathVariable(name = "book")long bookId,
            Model model) {
        //var comment = new CommentOnBookDto();
        //addBookDataToModel(model, ACTION_ADD, new BookDto());
        return "modify_comment";
    }

    @GetMapping("/comment/edit")
    public String editComment(
            @RequestParam(name = "id") long commentId,
            Model model) {
        var book = Optional.ofNullable(commentService.getCommentById(commentId))
                .orElseThrow(NoExistCommentException::new);
        //addBookDataToModel(model, ACTION_EDIT, book);
        return "modify_comment";
    }
}

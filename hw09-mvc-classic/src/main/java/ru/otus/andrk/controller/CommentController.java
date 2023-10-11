package ru.otus.andrk.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return "comment_list";
    }

    @GetMapping("/book/{book}/comment/add")
    public String addComment(
            @PathVariable(name = "book") long bookId,
            Model model) {
        var book = Optional.ofNullable(
                bookService.getBookById(bookId)).orElseThrow(NoExistBookException::new);
        var comment = new CommentOnBookDto(book);
        addCommentDataToModel(model, ACTION_ADD, comment);
        return "comment_edit";
    }

    @PostMapping("/book/{book}/comment/add")
    public String addComment(@Valid @ModelAttribute("comment") CommentOnBookDto comment,
                             BindingResult bindingResult,
                             Model model) {
        return processAddOrModifyComment(ACTION_ADD, comment, bindingResult, model);
    }

    @GetMapping("/comment/edit")
    public String editComment(
            @RequestParam(name = "id") long commentId,
            Model model) {
        var comment = Optional.ofNullable(commentService.getCommentById(commentId))
                .orElseThrow(NoExistCommentException::new);
        addCommentDataToModel(model, ACTION_EDIT, comment);
        return "comment_edit";
    }

    @PostMapping("/comment/edit")
    public String editComment(@Valid @ModelAttribute("comment") CommentOnBookDto comment,
                              BindingResult bindingResult,
                              Model model) {
        return processAddOrModifyComment(ACTION_EDIT, comment, bindingResult, model);
    }

    @GetMapping("/comment/delete")
    public String deleteComment(@RequestParam(name = "id") long commentId, Model model) {
        var bookId = getBookIdByCommentId(commentId);
        var book = Optional.ofNullable(bookService.getBookWithCommentsById(bookId))
                .orElseThrow(NoExistBookException::new);
        model.addAttribute("book", book);
        model.addAttribute("backUrl", "/book/" + bookId + "/comment");
        model.addAttribute("acceptUrl", "/comment/delete");
        model.addAttribute("delete", commentId);
        return "comment_list";
    }

    @PostMapping("/comment/delete")
    public String deleteComment(@RequestParam(name = "id") long commentId) {
        log.debug("delete comment id={}", commentId);
        var bookId = getBookIdByCommentId(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/book/" + bookId + "/comment";
    }

    private void addCommentDataToModel(Model model, String action, CommentOnBookDto comment) {
        model.addAttribute("action", action.equals(ACTION_ADD)
                ? "/book/" + comment.getBookId() + "/comment/add"
                : "/comment/edit");
        model.addAttribute("title", "comment." + action + "-title");
        model.addAttribute("comment", comment);
    }

    private String processAddOrModifyComment(String action,
                                             CommentOnBookDto comment,
                                             BindingResult bindingResult,
                                             Model model) {
        log.debug("{} {}", action, comment);
        if (bindingResult.hasErrors()) {
            addCommentDataToModel(model, action, comment);
            return "modify_comment";
        }

        if (action.equals(ACTION_ADD)) {
            commentService.addCommentForBook(comment.getBookId(), comment.getText());
        } else {
            commentService.modifyComment(comment.getId(), comment.getText());
        }
        return "redirect:/book/" + comment.getBookId() + "/comment";
    }

    private Long getBookIdByCommentId(long commentId) {
        return Optional.ofNullable(
                        commentService.getCommentById(commentId))
                .map(CommentOnBookDto::getBookId)
                .orElseThrow(NoExistCommentException::new);
    }
}

package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.andrk.excepton.NoExistBookException;
import ru.otus.andrk.excepton.NoExistCommentException;
import ru.otus.andrk.excepton.OtherLibraryManipulationException;
import ru.otus.andrk.service.CommentService;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {
    private final CommentService commentService;

    private final ConversionService conversionService;

    @ShellMethod(value = "List all comments for book",
            key = {"list all comments", "all comments", "list comments", "list comment", "comments"})
    public String getCommentsForBook(@ShellOption(help = "Book Id") long bookId) {
        try {
            var allComments = commentService.getCommentsForBook(bookId);
            return conversionService.convert(allComments, String.class);
        } catch (NoExistBookException e) {
            return "Error, Book with id=" + bookId + " not exist in library";
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't get comment list, see log for detail";
        }
    }

    @ShellMethod(value = "Get comment by id", key = {"get comment"})
    public String getCommentById(@ShellOption(help = "Comment Id") long commentId) {
        try {
            var comment = commentService.getCommentById(commentId);
            return conversionService.convert(comment, String.class);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't get comment by id, see log for detail";
        }
    }

    @ShellMethod(value = "Add new comment for book", key = {"add comment", "new comment"})
    public String addComment(
            @ShellOption(help = "Book Id") Long bookId,
            @ShellOption(help = "Text") String text) {
        try {
            var storedComment = commentService.addCommentForBook(bookId, text);
            return "Comment added\n" + conversionService.convert(storedComment, String.class);
        } catch (NoExistBookException e) {
            return "Error, Book with id=" + bookId + " not exist in library";
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't add comment, see log for detail";
        }
    }

    @ShellMethod(value = "Modify exist comment", key = {"modify comment", "update comment"})
    public String modifyComment(
            @ShellOption(help = "Comment Id") long commentId,
            @ShellOption(help = "Text") String text) {
        try {
            var storedComment = commentService.modifyComment(commentId, text);
            return "Comment changed, new state\n"
                    + conversionService.convert(storedComment, String.class);
        } catch (NoExistCommentException e) {
            return "Error, Comment with id=" + commentId + " not exist";
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't modify comment, see log for detail";
        }
    }

    @ShellMethod(value = "Delete comment by id", key = {"delete comment", "remove comment"})
    public String deleteCommentById(
            @ShellOption(help = "Comment Id") long commentId
    ) {
        try {
            commentService.deleteComment(commentId);
            return "Comment with id=" + commentId + " removed";
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't remove comment, see log for detail";
        }
    }
}

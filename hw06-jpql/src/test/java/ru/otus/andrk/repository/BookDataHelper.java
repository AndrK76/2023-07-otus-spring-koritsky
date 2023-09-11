package ru.otus.andrk.repository;

import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BookDataHelper {
    public static List<Book> getPredefinedBooks() {
        var ret = List.of(
                Book.builder().id(1L).name("Book 1")
                        .author(new Author(1, "unknown"))
                        .genre(new Genre(1, "unknown"))
                        .comments(List.of(
                                makeCommentWithId(1L, "Comment 1 for book 1"),
                                makeCommentWithId(2L, "Comment 2 for book 1")))
                        .build(),
                Book.builder().id(2L).name("Book without author and genre")
                        .comments(List.of(
                                makeCommentWithId(3L, "Comment 1 for book 2")))
                        .build(),
                Book.builder().id(3L).name("Book without genre")
                        .author(new Author(2, "known"))
                        .comments(List.of(
                                makeCommentWithId(4L, "Comment 1 for book 3"),
                                makeCommentWithId(5L, "Comment 2 for book 3"),
                                makeCommentWithId(6L, "Comment 3 for book 3")))
                        .build(),
                Book.builder().id(4L).name("Book without author")
                        .genre(new Genre(2, "known"))
                        .comments(new ArrayList<>())
                        .build()
        );
        ret.forEach(book -> {
            if (book.getComments() != null) {
                book.getComments().forEach(comm -> comm.setBook(book));
            }
        });
        return ret;
    }

    private static Comment makeCommentWithId(long id, String text) {
        Comment comment = new Comment();
        comment.setText(text);
        try {
            Field idField = Comment.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(comment, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return comment;
    }

    public static void setCommentId(Comment comment, long id) {
        try {
            Field idField = Comment.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(comment, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

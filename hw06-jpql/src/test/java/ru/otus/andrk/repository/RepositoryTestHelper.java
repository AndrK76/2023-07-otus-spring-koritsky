package ru.otus.andrk.repository;

import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryTestHelper {
    public static List<Book> getPredefinedBooks() {
        var ret = List.of(
                makeBook(1L, "Book 1",
                        new Author(1, "unknown"),
                        new Genre(1, "unknown"),
                        List.of(
                                makeCommentWithId(1L, "Comment 1 for book 1"),
                                makeCommentWithId(2L, "Comment 2 for book 1"))),
                makeBook(2L, "Book without author and genre", null, null,
                        List.of(
                                makeCommentWithId(3L, "Comment 1 for book 2"))),
                makeBook(3L, "Book without genre",
                        new Author(2, "known"), null,
                        List.of(
                                makeCommentWithId(4L, "Comment 1 for book 3"),
                                makeCommentWithId(5L, "Comment 2 for book 3"),
                                makeCommentWithId(6L, "Comment 3 for book 3"))),
                makeBook(4L, "Book without author",
                        null, new Genre(2, "known"),
                        new ArrayList<>())
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

    public static void setBookId(Book book, long id) {
        try {
            Field idField = Book.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(book, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

    public static Author copyAuthor(Author author) {
        return new Author(author.getId(), author.getName());
    }

    public static Genre copyGenre(Genre genre) {
        return new Genre(genre.getId(), genre.getName());
    }

    public static Comment copyComment(Comment comment) {
        var newComment = new Comment();
        newComment.setText(comment.getText());
        newComment.setBook(comment.getBook());
        setCommentId(newComment, comment.getId());
        return newComment;
    }

    public static Book copyBook(Book book) {
        return makeBook(book.getId(), book.getName(),
                copyAuthor(book.getAuthor()), copyGenre(book.getGenre()),
                book.getComments());
    }

    private static Book makeBook(long id, String name, Author author, Genre genre, List<Comment> comments) {
        var ret = new Book(name, author, genre);
        setBookId(ret, id);
        if (comments != null) {
            ret.setComments(comments.stream()
                    .map(c -> {
                        var comm = copyComment(c);
                        comm.setBook(ret);
                        return comm;
                    }).collect(Collectors.toList()));
        }
        return ret;
    }


}

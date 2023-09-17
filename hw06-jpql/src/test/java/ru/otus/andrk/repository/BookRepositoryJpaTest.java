package ru.otus.andrk.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.otus.andrk.repository.RepositoryTestHelper.copyBook;
import static ru.otus.andrk.repository.RepositoryTestHelper.getPredefinedBooks;
import static ru.otus.andrk.repository.RepositoryTestHelper.setBookId;
import static ru.otus.andrk.repository.RepositoryTestHelper.setCommentId;

@DataJpaTest
@Import(BookRepositoryJpa.class)
public class BookRepositoryJpaTest {

    private static final int EXPECTED_BOOK_COUNT = 4;

    @Autowired
    private BookRepository repo;

    @Autowired
    private TestEntityManager em;

    @Test
    public void shouldReturnCorrectBooksList() {
        var expectedList = getPredefinedBooks();
        var actualList = repo.findAll();
        actualList.forEach(em::detach);
        assertThat(actualList)
                .isNotNull()
                .hasSize(EXPECTED_BOOK_COUNT)
                .containsAll(expectedList);
    }

    @Test
    public void shouldLoadCorrectCommentsForListWhenNeeded() {
        var expectedList = getPredefinedBooks();
        var actualList = repo.findAll();
        actualList.forEach(r -> r.getComments().forEach(em::detach));
        actualList.forEach(em::detach);
        actualList.forEach(r -> {
            var expectedBook = expectedList.stream()
                    .filter(e -> e.getId() == r.getId())
                    .findFirst().orElseThrow();
            checkCommentForBook(r, expectedBook);
        });
    }


    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void shouldReturnExpectedBookById(long expectedId) {
        var expectedBook = getPredefinedBooks().stream()
                .filter(r -> r.getId() == expectedId).findFirst().orElseThrow();
        var actualBook = repo.findById(expectedId);
        assertThat(actualBook).isPresent().get().isEqualTo(expectedBook);
    }

    @Test
    public void shouldReturnEmptyForNoExistId() {
        var actualBook = repo.findById(9L);
        assertThat(actualBook).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void shouldLoadExpectedCommentsForBookWhenNeeded(long expectedBookId) {
        var expectedBook = getPredefinedBooks().stream()
                .filter(r -> r.getId() == expectedBookId).findFirst().orElseThrow();
        var actualBook = repo.findById(expectedBookId);
        assertThat(actualBook).isPresent();
        actualBook.get().getComments().forEach(em::detach);
        checkCommentForBook(actualBook.get(), expectedBook);
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var srcBook = copyBook(getPredefinedBooks().get(0));
        setBookId(srcBook, 0L);
        srcBook.getComments().forEach(c -> setCommentId(c, 0));

        var insertedBook = repo.save(srcBook);
        em.flush();

        assertThat(insertedBook.getId()).isNotEqualTo(0L);
        em.detach(insertedBook);
        em.detach(srcBook);

        var storedBook = em.find(Book.class, insertedBook.getId());
        assertThat(storedBook).isEqualTo(insertedBook);
    }

    @Test
    public void shouldUpdateAndReturnExpectedResult() {
        var srcBook = getPredefinedBooks().get(0);
        srcBook.setName("new name");
        srcBook.setAuthor(new Author(2, "known"));

        var updatedBook = repo.save(srcBook);
        em.flush();

        assertThat(updatedBook.getId()).isEqualTo(srcBook.getId());
        em.detach(updatedBook);
        var storedBook = em.find(Book.class, srcBook.getId());
        assertThat(storedBook).isEqualTo(srcBook);
    }

    @Test
    public void shouldUpdateToNullAuthorAndGenre() {
        var srcBook = getPredefinedBooks().get(0);
        srcBook.setAuthor(null);
        srcBook.setGenre(null);

        var updatedBook = repo.save(srcBook);
        em.flush();

        assertThat(updatedBook.getId()).isEqualTo(srcBook.getId());
        em.detach(updatedBook);
        var storedBook = em.find(Book.class, srcBook.getId());
        assertThat(storedBook).isEqualTo(srcBook);
    }

    @Test
    public void shouldUpdateFromNullAuthorAndGenre() {
        var srcBook = getPredefinedBooks().get(1);
        srcBook.setAuthor(new Author(1, "unknown"));
        srcBook.setGenre(new Genre(2, "known"));

        var updatedBook = repo.save(srcBook);
        em.flush();

        assertThat(updatedBook.getId()).isEqualTo(srcBook.getId());
        em.detach(updatedBook);
        var storedBook = em.find(Book.class, srcBook.getId());
        assertThat(storedBook).isEqualTo(srcBook);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenChangingToNoExistAuthor() {
        var srcBook = getPredefinedBooks().get(1);
        srcBook.setAuthor(new Author(3, "three"));

        assertThatThrownBy(() -> {
            repo.save(srcBook);
            em.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("AUTHOR");
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenChangingToNoExistGenre() {
        var srcBook = getPredefinedBooks().get(1);
        srcBook.setGenre(new Genre(3, "three"));

        assertThatThrownBy(() -> {
            repo.save(srcBook);
            em.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("GENRE");
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenAddingBookWithNoExistAuthor() {
        var srcBook = getPredefinedBooks().get(0);
        setBookId(srcBook, 5L);
        srcBook.setAuthor(new Author(3, "three"));

        assertThatThrownBy(() -> {
            repo.save(srcBook);
            em.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("AUTHOR");
    }

    @Test
    public void shouldThrowConstraintViolationExceptionWhenAddingBookWithNoExistGenre() {
        var srcBook = getPredefinedBooks().get(0);
        setBookId(srcBook, 5L);
        srcBook.setGenre(new Genre(3, "three"));

        assertThatThrownBy(() -> {
            repo.save(srcBook);
            em.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("GENRE");
    }

    @Test
    public void shouldDeleteBookFromStore() {
        var bookForDelete = em.find(Book.class, getPredefinedBooks().get(0).getId());
        assertThat(bookForDelete).isNotNull();

        repo.delete(bookForDelete);
        em.flush();
        em.detach(bookForDelete);

        var bookAfterDelete = em.find(Book.class, getPredefinedBooks().get(0).getId());
        assertThat(bookAfterDelete).isNull();
    }

    @Test
    public void shouldDeleteCommentsWhenDeleteBook() {
        var bookForDelete = em.find(Book.class, getPredefinedBooks().get(0).getId());
        var commentIds = bookForDelete.getComments()
                .stream().map(Comment::getId).toList();
        em.detach(bookForDelete);

        for (var commentId : commentIds) {
            var comment = em.find(Comment.class, commentId);
            assertThat(comment).isNotNull();
            em.detach(comment);
        }

        bookForDelete = em.find(Book.class, getPredefinedBooks().get(0).getId());
        repo.delete(bookForDelete);
        em.flush();
        em.detach(bookForDelete);

        for (var commentId : commentIds) {
            var comment = em.find(Comment.class, commentId);
            assertThat(comment).isNull();
        }
    }

    private void checkCommentForBook(Book actualBook, Book expectedBook) {
        if (expectedBook.getComments() == null || expectedBook.getComments().isEmpty()) {
            assertThat(actualBook.getComments()).isEmpty();
        } else {
            assertThat(actualBook.getComments())
                    .hasSize(expectedBook.getComments().size())
                    .containsAll(expectedBook.getComments());
        }
    }

}

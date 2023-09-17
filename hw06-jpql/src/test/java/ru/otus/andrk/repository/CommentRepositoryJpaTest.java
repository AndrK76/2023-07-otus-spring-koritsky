package ru.otus.andrk.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.andrk.repository.RepositoryTestHelper.getPredefinedBooks;
import static ru.otus.andrk.repository.RepositoryTestHelper.setCommentId;

@DataJpaTest
@Import(CommentRepositoryJpa.class)
public class CommentRepositoryJpaTest {
    @Autowired
    private CommentRepository repo;

    @Autowired
    private TestEntityManager em;

    @Test
    public void shouldReturnCorrectCommentsListForBook() {
        var expectedBookList = getPredefinedBooks();
        expectedBookList.forEach(em::merge);
        for (var book : expectedBookList) {
            var expectedList = book.getComments();
            var actualList = repo.findCommentsForBook(book);
            assertThat(actualList)
                    .isNotNull()
                    .hasSize(expectedList.size())
                    .containsAll(expectedList);
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6})
    public void shouldReturnExpectedCommentById(long expectedId) {
        var expectedComment = getPredefinedBooks().stream()
                .flatMap(c -> c.getComments().stream())
                .filter(r -> r.getId() == expectedId)
                .findFirst().orElseThrow();
        var actualComment = repo.findCommentById(expectedComment.getId());
        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    public void shouldReturnEmptyForNoExistId() {
        var actualComment = repo.findCommentById(9L);
        assertThat(actualComment).isEmpty();
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var srcComment = getPredefinedBooks().get(0).getComments().get(0);
        setCommentId(srcComment, 0L);

        var insertedComment = repo.save(srcComment);
        em.flush();

        assertThat(insertedComment.getId()).isNotEqualTo(0L);
        em.detach(insertedComment);
        em.detach(srcComment);
        var storedComment = em.find(Comment.class, insertedComment.getId());
        assertThat(storedComment).isEqualTo(insertedComment);
    }

    @Test
    public void shouldUpdateAndReturnExpectedResult() {
        var srcComment = getPredefinedBooks().get(0).getComments().get(0);
        srcComment.setText("new text");

        var updatedComment = repo.save(srcComment);
        em.flush();

        assertThat(updatedComment.getId()).isEqualTo(srcComment.getId());
        em.detach(updatedComment);
        var storedComment = em.find(Comment.class, srcComment.getId());
        assertThat(storedComment).isEqualTo(srcComment);
    }

    @Test
    public void shouldDeleteCommentFromStore() {
        var commentId = getPredefinedBooks().get(0).getComments().get(0).getId();
        var commentForDelete = em.find(Comment.class, commentId);
        assertThat(commentForDelete).isNotNull();

        repo.delete(commentForDelete);
        em.flush();
        em.detach(commentForDelete);

        var commentAfterDelete = em.find(Comment.class, commentId);
        assertThat(commentAfterDelete).isNull();
    }

    @Test
    public void shouldDontChangeBookWhenRemovingComment() {
        var commentId = getPredefinedBooks().get(0).getComments().get(0).getId();
        var commentForDelete = em.find(Comment.class, commentId);
        var bookBeforeDelete = commentForDelete.getBook();
        var hash = bookBeforeDelete.hashCode();
        em.detach(bookBeforeDelete);

        repo.delete(commentForDelete);
        em.flush();
        em.detach(commentForDelete);

        var bookAfterDelete = em.find(Book.class, bookBeforeDelete.getId());
        assertThat(bookBeforeDelete == bookAfterDelete).isFalse();
        assertThat(bookBeforeDelete).isEqualTo(bookAfterDelete);
    }
}

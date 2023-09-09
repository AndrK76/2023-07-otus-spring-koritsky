package ru.otus.andrk.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.andrk.exception.NoExistAuthorException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistGenreException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc dao;

    private List<Book> predefinedBooks;

    @BeforeEach
    void initPredefinedBooks() {
        predefinedBooks = List.of(
                new Book(1, "Book 1",
                        new Author(1, "unknown"),
                        new Genre(1, "unknown")),
                new Book(2, "Book without author and genre"),
                new Book(3, "Book without genre",
                        new Author(2, "known"),
                        null),
                new Book(4, "Book without author",
                        null,
                        new Genre(2, "known"))
        );
    }

    @Test
    public void shouldReturnExpectedBookList() {
        var actualList = dao.getAll();
        assertThat(actualList)
                .isNotNull()
                .hasSize(4)
                .containsAll(predefinedBooks);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void shouldReturnExpectedBookById(long expectedId) {
        var expectedBook = predefinedBooks.stream()
                .filter(r -> r.getId() == expectedId).findFirst().orElseThrow();
        var actualBook = dao.getById(expectedId);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldReturnNullForNoExistId() {
        var actualBook = dao.getById(9);
        assertThat(actualBook).isNull();
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var srcBook = predefinedBooks.get(0);
        var expectedBook = new Book(0, srcBook.getName(), srcBook.getAuthor(), srcBook.getGenre());
        long newId = dao.insert(expectedBook);
        expectedBook = new Book(newId, srcBook.getName(), srcBook.getAuthor(), srcBook.getGenre());
        var actualBook = dao.getById(newId);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldUpdateAndReturnExpectedResult() {
        var expectedBook = predefinedBooks.get(0);
        expectedBook.setName("new name");
        expectedBook.setAuthor(new Author(2, "known"));
        dao.update(expectedBook);
        var actualBook = dao.getById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldUpdateToNullAuthorAndGenre() {
        var expectedBook = predefinedBooks.get(0);
        expectedBook.setAuthor(null);
        expectedBook.setGenre(null);
        dao.update(expectedBook);
        var actualBook = dao.getById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldUpdateFromNullAuthorAndGenre() {
        var expectedBook = predefinedBooks.get(1);
        expectedBook.setAuthor(new Author(1, "unknown"));
        expectedBook.setGenre(new Genre(2, "known"));
        dao.update(expectedBook);
        var actualBook = dao.getById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldThrowNoExistBookExceptionForChangingBookWithNoExistId() {
        var srcBook = predefinedBooks.get(0);
        long newId = 9L;
        var expectedBook = new Book(newId, srcBook.getName(), srcBook.getAuthor(), srcBook.getGenre());
        assertThatThrownBy(() -> dao.update(expectedBook)).isInstanceOf(NoExistBookException.class);
    }

    @Test
    public void shouldThrowNoExistAuthorExceptionWhenChangingToNoExistAuthor() {
        var expectedBook = predefinedBooks.get(1);
        expectedBook.setAuthor(new Author(3, "three"));
        assertThatThrownBy(() -> dao.update(expectedBook)).isInstanceOf(NoExistAuthorException.class);
    }

    @Test
    public void shouldThrowNoExistGenreExceptionWhenChangingToNoExistGenre() {
        var expectedBook = predefinedBooks.get(1);
        expectedBook.setGenre(new Genre(3, "three"));
        assertThatThrownBy(() -> dao.update(expectedBook)).isInstanceOf(NoExistGenreException.class);
    }

    @Test
    public void shouldThrowNoExistAuthorExceptionWhenAddingBookWithNoExistAuthor() {
        var srcBook = predefinedBooks.get(0);
        var expectedBook = new Book(5L, srcBook.getName(), new Author(3, "three"), null);
        assertThatThrownBy(() -> dao.insert(expectedBook)).isInstanceOf(NoExistAuthorException.class);
    }

    @Test
    public void shouldThrowNoExistGenreExceptionWhenAddingBookWithNoExistGenre() {
        var srcBook = predefinedBooks.get(0);
        var expectedBook = new Book(5L, srcBook.getName(), null, new Genre(3, "three"));
        assertThatThrownBy(() -> dao.insert(expectedBook)).isInstanceOf(NoExistGenreException.class);
    }

    @Test
    public void shouldDeleteBookFromStore() {
        int countBefore = dao.getAll().size();
        dao.delete(predefinedBooks.get(0).getId());
        int countAfter = dao.getAll().size();
        assertThat(countBefore - countAfter).isEqualTo(1);
        var nullBook = dao.getById(predefinedBooks.get(0).getId());
        assertThat(nullBook).isNull();
    }

}

package ru.otus.andrk.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Transactional
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTest {

    @Autowired
    private BookDaoJdbc dao;

    private List<Book> predefinedBooks;

    @BeforeEach
    void initPredefinedBooks() {
        predefinedBooks = List.of(
                new Book(0, "Book 0",
                        new Author(0, "unknown"),
                        new Genre(0, "unknown")),
                new Book(1, "Book without author and genre"),
                new Book(2, "Book without genre",
                        new Author(1, "known"),
                        null),
                new Book(3, "Book without author",
                        null,
                        new Genre(1, "known"))
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
    @ValueSource(longs = {0, 1, 2, 3})
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
    public void shouldThrowAlreadyExistObjExceptionForAddingBookWithExistId() {
        var book = predefinedBooks.get(0);
        assertThatThrownBy(() -> dao.insert(book)).isInstanceOf(AlreadyExistObjectException.class);
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var srcBook = predefinedBooks.get(0);
        long newId = 9L;
        var expectedBook = new Book(newId, srcBook.getName(), srcBook.getAuthor(), srcBook.getGenre());
        assertThat(dao.getById(newId)).isNull();
        dao.insert(expectedBook);
        var actualBook = dao.getById(newId);
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldUpdateAndReturnExpectedResult() {
        var expectedBook = predefinedBooks.get(0);
        expectedBook.setName("new name");
        expectedBook.setAuthor(new Author(1, "known"));
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
        expectedBook.setAuthor(new Author(0,"unknown"));
        expectedBook.setGenre(new Genre(1,"known"));
        dao.update(expectedBook);
        var actualBook = dao.getById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    public void shouldThrowNoExistObjExceptionForChangingBookWithNoExistId() {
        var srcBook = predefinedBooks.get(0);
        long newId = 9L;
        var expectedBook = new Book(newId, srcBook.getName(), srcBook.getAuthor(), srcBook.getGenre());
        assertThatThrownBy(() -> dao.update(expectedBook)).isInstanceOf(NoExistObjectException.class);
    }

}

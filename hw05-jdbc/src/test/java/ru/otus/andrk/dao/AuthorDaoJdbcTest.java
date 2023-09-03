package ru.otus.andrk.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Genre;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(AuthorDaoJdbc.class)
@Transactional
public class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDaoJdbc dao;

    private final Map<Long, String> expectedAuthors = Map.of(
            0L, "unknown",
            1L, "known");

    @Test
    public void shouldReturnExpectedAuthorList() {
        var actualList = dao.getAll();
        var expectedList = expectedAuthors.entrySet().stream()
                .map(r -> new Author(r.getKey(), r.getValue()))
                .collect(Collectors.toList());
        assertThat(actualList)
                .isNotNull()
                .hasSize(2)
                .containsAll(expectedList);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    public void shouldReturnExpectedAuthorById(long expectedId) {
        var expectedAuthor = new Author(expectedId, expectedAuthors.get(expectedId));
        var actualAuthor = dao.getById(expectedId);
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

    @Test
    public void shouldReturnNullForNoExistId(){
        var actualAuthor = dao.getById(9);
        assertThat(actualAuthor).isNull();
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var expectedAuthor = new Author(3, "test");
        assertThat(dao.getById(3L)).isNull();
        dao.insert(expectedAuthor);
        var actualAuthor = dao.getById(3L);
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

}

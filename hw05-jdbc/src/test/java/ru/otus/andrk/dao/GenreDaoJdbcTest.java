package ru.otus.andrk.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.model.Genre;

import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(GenreDaoJdbc.class)
@Transactional
public class GenreDaoJdbcTest {

    @Autowired
    private GenreDaoJdbc dao;

    private final Map<Long, String> expectedGenres = Map.of(
            0L, "unknown",
            1L, "known");

    @Test
    public void shouldReturnExpectedGenreList() {
        var actualList = dao.getAll();
        var expectedList = expectedGenres.entrySet().stream()
                .map(r -> new Genre(r.getKey(), r.getValue()))
                .collect(Collectors.toList());
        assertThat(actualList)
                .isNotNull()
                .hasSize(2)
                .containsAll(expectedList);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    public void shouldReturnExpectedGenreById(long expectedId) {
        var expectedGenre = new Genre(expectedId, expectedGenres.get(expectedId));
        var actualGenre = dao.getById(expectedId);
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @Test
    public void shouldReturnNullForNoExistId(){
        var actualGenre = dao.getById(9);
        assertThat(actualGenre).isNull();
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var expectedGenre = new Genre(3, "test");
        assertThat(dao.getById(3L)).isNull();
        dao.insert(expectedGenre);
        var actualGenre = dao.getById(3L);
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

}

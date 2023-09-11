package ru.otus.andrk.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Genre;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenreRepositoryJpa.class)
public class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepository repo;

    @Autowired
    private TestEntityManager em;

    private final Map<Long, String> expectedGenres = Map.of(
            1L, "unknown",
            2L, "known");

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    public void shouldFindExpectedGenreById(long id) {
        var expectedGenre = em.find(Genre.class, id);
        var actualGenre = repo.findById(id);
        assertThat(actualGenre).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    public void shouldReturnCorrectGenresList() {
        var actualList = repo.findAll();
        var expectedList = expectedGenres.entrySet().stream()
                .map(r -> new Genre(r.getKey(), r.getValue())).toList();
        assertThat(actualList)
                .isNotNull()
                .hasSize(2)
                .containsAll(expectedList);
    }

    @Test
    public void shouldReturnEmptyForNoExistId() {
        var actualGenre = repo.findById(9);
        assertThat(actualGenre).isEmpty();
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var expectedGenre = new Genre(0, "test");
        var storedResult = repo.save(expectedGenre.copy());
        assertThat(storedResult.getName()).isEqualTo(expectedGenre.getName());
        var storedId = storedResult.getId();
        assertThat(storedId).isNotEqualTo(0L);
        em.detach(storedResult);
        var storedGenre = em.find(Genre.class, storedId);
        assertThat(storedGenre.getName()).isEqualTo(expectedGenre.getName());
    }
}

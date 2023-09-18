package ru.otus.andrk.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.andrk.model.Author;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepository repo;

    @Autowired
    private TestEntityManager em;

    private final Map<Long, String> expectedAuthors = Map.of(
            1L, "unknown",
            2L, "known");

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    public void shouldFindExpectedAuthorById(long id) {
        var expectedAuthor = em.find(Author.class, id);
        var actualAuthor = repo.findById(id);
        assertThat(actualAuthor).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    public void shouldReturnCorrectAuthorsList() {
        var actualList = repo.findAll();
        var expectedList = expectedAuthors.entrySet().stream()
                .map(r -> new Author(r.getKey(), r.getValue())).toList();
        assertThat(actualList)
                .isNotNull()
                .hasSize(2)
                .containsAll(expectedList);
    }

    @Test
    public void shouldReturnEmptyForNoExistId() {
        var actualAuthor = repo.findById(9L);
        assertThat(actualAuthor).isEmpty();
    }

    @Test
    public void shouldAddAndReturnExpectedResult() {
        var expectedAuthor = new Author(0, "test");
        var storedResult = repo.save(expectedAuthor);
        assertThat(storedResult.getName()).isEqualTo(expectedAuthor.getName());
        var storedId = storedResult.getId();
        assertThat(storedId).isNotEqualTo(0L);
        em.detach(storedResult);
        var storedAuthor = em.find(Author.class, storedId);
        assertThat(storedAuthor.getName()).isEqualTo(expectedAuthor.getName());
    }
}

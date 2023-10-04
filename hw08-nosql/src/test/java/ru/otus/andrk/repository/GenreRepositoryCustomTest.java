package ru.otus.andrk.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.util.SequenceGenerator;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@DataMongoTest
@ComponentScan(basePackageClasses = {GenreRepositoryCustomImpl.class})
class GenreRepositoryCustomTest {

    @Autowired
    MongoTemplate template;

    @Autowired
    GenreRepositoryCustomImpl repository;

    @MockBean
    SequenceGenerator generator;

    private static final AtomicLong sequenceValue = new AtomicLong(99L);

    @BeforeEach
    public void initGenerator() {
        when(generator.getValue(Mockito.anyString()))
                .thenAnswer(i -> sequenceValue.incrementAndGet());
    }

    @Test
    public void shouldSetAutoincrementIdToGenreWhen_insertGenre() {
        synchronized (sequenceValue) {
            var lastId = generator.getValue(Genre.SEQUENCE_NAME);
            var actualGenre = repository.insertGenre(new Genre("Новый жанр"));

            assertThat(actualGenre)
                    .isNotNull()
                    .returns(lastId + 1L, Genre::getId);
        }
    }

    @Test
    public void shouldInsertOnlyOneDocumentWhen_insertGenre() {
        var newGenre = repository.insertGenre(new Genre("Новый жанр"));
        var actualGenres = getGenresById(newGenre.getId());
        assertThat(actualGenres).hasSize(1);
    }

    @Test
    public void shouldCallSequenceGeneratorWhen_insertGenre() {
        repository.insertGenre(new Genre("Новый жанр"));
        verify(generator, times(1)).getValue(any());
    }


    private List<Genre> getGenresById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Genre.class);
    }
}
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
import ru.otus.andrk.model.Author;
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
@ComponentScan(basePackageClasses = {AuthorRepositoryCustomImpl.class})
class AuthorRepositoryCustomTest {

    @Autowired
    MongoTemplate template;

    @Autowired
    AuthorRepositoryCustomImpl repository;

    @MockBean
    SequenceGenerator generator;

    private static final AtomicLong sequenceValue = new AtomicLong(99L);

    @BeforeEach
    public void initGenerator() {
        when(generator.getValue(Mockito.anyString()))
                .thenAnswer(i -> sequenceValue.incrementAndGet());
    }

    @Test
    public void shouldSetAutoincrementIdToAuthorWhen_insertAuthor() {
        synchronized (sequenceValue) {
            var lastId = generator.getValue(Author.SEQUENCE_NAME);
            var actualAuthor = repository.insertAuthor(new Author("Новый автор"));

            assertThat(actualAuthor)
                    .isNotNull()
                    .returns(lastId + 1L, Author::getId);
        }
    }

    @Test
    public void shouldInsertOnlyOneDocumentWhen_insertAuthor() {
        var newAuthor = repository.insertAuthor(new Author("Новый автор"));
        var actualAuthors = getAuthorsById(newAuthor.getId());
        assertThat(actualAuthors).hasSize(1);
    }

    @Test
    public void shouldCallSequenceGeneratorWhen_insertAuthor() {
        repository.insertAuthor(new Author("Новый автор"));
        verify(generator, times(1)).getValue(any());
    }


    private List<Author> getAuthorsById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Author.class);
    }
}
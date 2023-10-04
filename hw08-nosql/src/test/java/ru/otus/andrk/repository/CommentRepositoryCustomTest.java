package ru.otus.andrk.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.util.SequenceGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@DataMongoTest
@ComponentScan(basePackageClasses = {CommentRepositoryCustomImpl.class})
public class CommentRepositoryCustomTest {
    @Autowired
    MongoTemplate template;

    @Autowired
    CommentRepositoryCustomImpl repository;

    @MockBean
    SequenceGenerator generator;

    private static final AtomicLong sequenceValue = new AtomicLong(99L);

    @BeforeEach
    public void initGenerator() {
        when(generator.getValue(Mockito.anyString()))
                .thenAnswer(i -> sequenceValue.incrementAndGet());
    }

    @Test
    public void shouldSetAutoincrementIdToCommentWhen_insertComment() {
        synchronized (sequenceValue) {
            var lastId = generator.getValue(Comment.SEQUENCE_NAME);
            var actualComment = repository.insertComment(createComment());

            assertThat(actualComment)
                    .isNotNull()
                    .returns(lastId + 1L, Comment::getId);
        }
    }

    @Test
    public void shouldInsertOnlyOneDocumentWhen_insertComment() {
        var newComment = repository.insertComment(createComment());
        var actualComments = getCommentsById(newComment.getId());
        assertThat(actualComments).hasSize(1);
    }

    @Test
    public void shouldCallSequenceGeneratorWhen_insertComment() {
        repository.insertComment(createComment());
        verify(generator, times(1)).getValue(any());
    }

    @Test
    public void shouldInsertAndReturnCorrectCommentWhen_insertComment() {
        var expectedBook = getBookById(3L);
        assert expectedBook != null;
        var sourceComment = new Comment("текст", expectedBook);

        var actualComment = repository.insertComment(sourceComment);
        assertThatCommentsHaveEqualContent(actualComment, sourceComment);

        var storedComment = getCommentsById(actualComment.getId()).get(0);
        assertThatCommentsHaveEqualContent(storedComment, sourceComment);
    }

    @ParameterizedTest
    @MethodSource("getCommentsForBookParams")
    public void shouldFindExpectedCommentsWhen_findCommentsByBookId(
            long bookId, List<Comment> expectedComments) {
        var actualComments = repository.findCommentsByBookId(bookId);
        assertThat(actualComments)
                .hasSize(expectedComments.size())
                .usingElementComparator((o1, o2) -> {
                    if (o1.getId() == o2.getId() && o1.getText().equals(o2.getText())
                            && o1.getBook().getId() == o2.getBook().getId()) {
                        return 0;
                    }
                    return 1;
                })
                .containsAll(expectedComments);
    }


    List<Comment> getCommentsById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Comment.class);
    }

    private Comment createComment() {
        return new Comment("Новый коммент", createBook());
    }

    private Book createBook() {
        return new Book(99L, "Новая книга",
                new Author(1L, "автор 1"),
                new Genre(1L, "жанр 1"));
    }

    private Book getBookById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Book.class)
                .stream().findFirst().orElse(null);
    }

    private void assertThatCommentsHaveEqualContent(Comment actualComment, Comment sourceComment) {
        assertThat(actualComment.getBook()).isNotNull();
        assertThat(actualComment)
                .returns(sourceComment.getText(), Comment::getText)
                .returns(sourceComment.getBook().getId(), r -> r.getBook().getId());
    }

    private static Stream<Arguments> getCommentsForBookParams() {
        return Stream.of(
                Arguments.of(
                        1L, List.of(
                                new Comment(1, "Comment 1 for book wag",
                                        new Book(1L, "", null, null)),
                                new Comment(1, "Comment 1 for book wag",
                                        new Book(1L, "", null, null))
                        )
                ),
                Arguments.of(4L, new ArrayList<Comment>())
        );

    }
}
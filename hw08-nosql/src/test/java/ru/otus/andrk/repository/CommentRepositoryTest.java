package ru.otus.andrk.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@DataMongoTest
@ComponentScan(basePackageClasses = {CommentRepository.class})
public class CommentRepositoryTest {
    @Autowired
    MongoTemplate template;

    @Autowired
    CommentRepository repository;

    @MockBean
    CommentRepositoryCustom customRepo;

    @Test
    public void shouldDontDeleteBookWhen_deleteComment() {
        long commentId = 1L;
        var comment = repository.findById(commentId).orElseThrow();
        var book = comment.getBook();

        repository.delete(comment);

        var actualBook = getBookById(book.getId());
        assertThat(actualBook).isNotNull();

        var actualComment = getCommentById(commentId);
        assertThat(actualComment).isNull();
    }

    private Book getBookById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Book.class)
                .stream().findFirst().orElse(null);
    }

    private Comment getCommentById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Comment.class)
                .stream().findFirst().orElse(null);
    }
}

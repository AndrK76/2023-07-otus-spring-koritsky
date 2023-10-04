package ru.otus.andrk.repository;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.repository.util.SequenceGenerator;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final MongoTemplate template;

    private final SequenceGenerator sequenceGenerator;

    @Override
    public Comment insertComment(Comment comment) {
        var id = sequenceGenerator.getValue(Comment.SEQUENCE_NAME);
        return template.insert(new Comment(id, comment.getText(), comment.getBook()));
    }

    @Override
    public List<Comment> findCommentsByBookId(long bookId) {
        var bookWhere = new Document().append("$ref", "books").append("$id", bookId);
        return template.find(
                query(Criteria.where("book").is(bookWhere)),
                Comment.class);
    }

}

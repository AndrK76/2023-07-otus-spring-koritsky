package ru.otus.andrk.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final MongoTemplate template;
    private final SequenceGenerator sequenceGenerator;

    @Override
    public Comment insertComment(Comment comment){
        var id = sequenceGenerator.getValue(Comment.SEQUENCE_NAME);
        return template.insert(new Comment(id, comment.getText(), comment.getBook()));
    }

}

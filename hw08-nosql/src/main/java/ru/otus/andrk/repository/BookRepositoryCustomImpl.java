package ru.otus.andrk.repository;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate template;
    private final SequenceGenerator sequenceGenerator;

    @Override
    public Book insertBook(Book book) {
        var id = sequenceGenerator.getValue(Book.SEQUENCE_NAME);
        return template.insert(new Book(id, book.getName(), book.getAuthor(), book.getGenre()));
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        var bookWhere = new Document().append("$ref", "books").append("$id", bookId);
        var bookQuery = Query.query(Criteria.where("$id").is(bookId));
        var commentsQuery = Query.query(Criteria.where("book").is(bookWhere));
        template.remove(commentsQuery,Comment.class);
        template.remove(bookQuery,Book.class);
    }

    @Override
    @Transactional
    public void deleteBook(Book book) {
        deleteBook(book.getId());
    }

}

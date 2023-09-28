package ru.otus.andrk.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final MongoTemplate template;
    private final SequenceGenerator sequenceGenerator;

    @Override
    public Book insertBook(Book book) {
        var id = sequenceGenerator.getValue(Book.SEQUENCE_NAME);
        return template.insert(new Book(id, book.getName(), book.getAuthor(), book.getGenre()));
    }

}

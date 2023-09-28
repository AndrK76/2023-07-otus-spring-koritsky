package ru.otus.andrk.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.andrk.model.Author;

@RequiredArgsConstructor
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

    private final MongoTemplate template;
    private final SequenceGenerator sequenceGenerator;

    @Override
    public Author insertAuthor(Author author) {
        var id = sequenceGenerator.getValue(Author.SEQUENCE_NAME);
        return template.insert(new Author(id, author.getName()));
    }
}

package ru.otus.andrk.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Genre;

@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {

    private final MongoTemplate template;
    private final SequenceGenerator sequenceGenerator;

    @Override
    public Genre insertGenre(Genre genre)  {
        var id = sequenceGenerator.getValue(Genre.SEQUENCE_NAME);
        return template.insert(new Genre(id, genre.getName()));
    }

}

package ru.otus.andrk.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.nosql.AuthorMongo;
import ru.otus.andrk.model.nosql.GenreMongo;
import ru.otus.andrk.model.sql.AuthorRdb;
import ru.otus.andrk.model.sql.GenreRdb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class TransformIdService {
    private final Map<String, Long> authorIdMapping = new ConcurrentHashMap<>();

    private final Map<String, Long> genreIdMapping = new ConcurrentHashMap<>();

    public AuthorRdb transform(AuthorMongo author) {
        return new AuthorRdb(author.getName(), author.getId());
    }

    public GenreRdb transform(GenreMongo genre) {
        return new GenreRdb(genre.getName(), genre.getId());
    }
}

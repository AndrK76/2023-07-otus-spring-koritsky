package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.nosql.AuthorMongo;
import ru.otus.andrk.model.nosql.BookMongo;
import ru.otus.andrk.model.nosql.CommentMongo;
import ru.otus.andrk.model.nosql.GenreMongo;
import ru.otus.andrk.model.sql.AuthorRdb;
import ru.otus.andrk.model.sql.BookRdb;
import ru.otus.andrk.model.sql.CommentRdb;
import ru.otus.andrk.model.sql.GenreRdb;

@Service
@Log4j2
@RequiredArgsConstructor
public class TransformIdService {

    private final IdMappingService idMappingService;

    public AuthorRdb transform(AuthorMongo author) {
        return new AuthorRdb(author.getName(), author.getId());
    }

    public GenreRdb transform(GenreMongo genre) {
        return new GenreRdb(genre.getName(), genre.getId());
    }

    public BookRdb transform(BookMongo book) {
        var builder = BookRdb.builder()
                .name(book.getName())
                .mongoId(book.getId());
        if (book.getAuthor() != null) {
            builder.authorId(idMappingService.getKey("AUTHOR",
                    book.getAuthor().getId()));
        }
        if (book.getGenre() != null) {
            builder.genreId(idMappingService.getKey("GENRE",
                    book.getGenre().getId()));
        }
        return builder.build();
    }

    public CommentRdb transform(CommentMongo comment) {
        return new CommentRdb(
                comment.getText(), comment.getId(),
                idMappingService.getKey("BOOK", comment.getBook().getId()));
    }

}

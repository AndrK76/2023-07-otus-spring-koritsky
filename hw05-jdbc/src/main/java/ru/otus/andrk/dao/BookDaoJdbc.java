package ru.otus.andrk.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.exception.NoExistAuthorException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistGenreException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Book> getAll() {
        String statement = "select b.id, b.name, a.id as author_id, a.name as author_name" +
                ", g.id as genre_id, g.name as genre_name" +
                " from books b" +
                " left outer join authors a on a.id=b.author_id" +
                " left outer join genres g on g.id=b.genre_id";
        return jdbc.query(statement, new BookMapper());
    }

    @Override
    public Book getById(long id) {
        try {
            String statement = "select b.id, b.name, a.id as author_id, a.name as author_name" +
                    ", g.id as genre_id, g.name as genre_name" +
                    " from books b" +
                    " left outer join authors a on a.id=b.author_id" +
                    " left outer join genres g on g.id=b.genre_id" +
                    " where b.id=:id";
            return jdbc.queryForObject(statement, Map.of("id", id), new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public long insert(Book book) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update("insert into books (name, author_id, genre_id)" +
                            " values (:name, :author_id, :genre_id)", makeParams(book, false)
                    , keyHolder, new String[]{"id"});
            return keyHolder.getKey().longValue();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().toLowerCase().contains("book_author_fk")) {
                throw new NoExistAuthorException(e);
            } else if (e.getMessage().toLowerCase().contains("book_genre_fk")) {
                throw new NoExistGenreException(e);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void update(Book book) {
        try {
            var processed = jdbc.update("update books set name=:name, author_id=:author_id, genre_id=:genre_id"
                    + " where id=:id", makeParams(book, true));
            if (processed == 0) {
                throw new NoExistBookException();
            }
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().toLowerCase().contains("book_author_fk")) {
                throw new NoExistAuthorException(e);
            } else if (e.getMessage().toLowerCase().contains("book_genre_fk")) {
                throw new NoExistGenreException(e);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void delete(long id) {
        jdbc.update("delete from books where id=:id", Map.of("id", id));
    }

    private MapSqlParameterSource makeParams(Book book, boolean includeIdParam) {
        MapSqlParameterSource ret = new MapSqlParameterSource();
        ret.addValue("name", book.getName());
        ret.addValue("author_id", book.getAuthor() == null ? null : book.getAuthor().id());
        ret.addValue("genre_id", book.getGenre() == null ? null : book.getGenre().id());
        if (includeIdParam) {
            ret.addValue("id", book.getId());
        }
        return ret;
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            Book book = new Book(id, name);
            Long genreId = rs.getObject("genre_id", Long.class);
            if (genreId != null) {
                String genreName = rs.getString("genre_name");
                book.setGenre(new Genre(genreId, genreName));
            }
            Long authorId = rs.getObject("author_id", Long.class);
            if (authorId != null) {
                String authorName = rs.getString("author_name");
                book.setAuthor(new Author(authorId, authorName));
            }
            return book;
        }
    }
}

package ru.otus.andrk.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Book> getAll() {
        return jdbc.query(makeCommonSelectPhrase(), new BookMapper());
    }

    @Override
    public Book getById(long id) {
        try {
            return jdbc.queryForObject(makeCommonSelectPhrase() + " where b.id=:id",
                    Map.of("id", id), new BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void insert(Book book) {
        try {
            jdbc.update("insert into books (id, name, author_id, genre_id)" +
                    " values (:id, :name, :author_id, :genre_id)", makeParams(book));
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistObjectException(e);
        }
    }

    @Override
    public void update(Book book) {
        var processed = jdbc.update("update books set name=:name, author_id=:author_id, genre_id=:genre_id"
                + " where id=:id", makeParams(book));
        if (processed == 0) {
            throw new NoExistObjectException();
        }
    }

    @Override
    public void delete(Book book) {
        jdbc.update("delete from books where id=:id", Map.of("id", book.getId()));
    }

    private String makeCommonSelectPhrase() {
        return "select b.id, b.name" +
                ", a.id as author_id, a.name as author_name" +
                ", g.id as genre_id, g.name as genre_name" +
                " from books b" +
                " left outer join authors a on a.id=b.author_id" +
                " left outer join genres g on g.id=b.genre_id";
    }

    private Map<String, Object> makeParams(Book book) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("id", book.getId());
        ret.put("name", book.getName());
        ret.put("author_id", book.getAuthor() == null ? null : book.getAuthor().id());
        ret.put("genre_id", book.getGenre() == null ? null : book.getGenre().id());
        return Collections.unmodifiableMap(ret);
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

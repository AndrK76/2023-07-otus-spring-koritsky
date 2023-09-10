package ru.otus.andrk.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, name from authors",
                new AuthorMapper());
    }

    @Override
    public Author getById(long id) {
        try {
            return jdbc.queryForObject("select id, name from authors where id=:id",
                    Map.of("id", id), new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public long insert(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", author.name());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into authors (name) values (:name)", params,
                keyHolder, new String[]{"id"});
        return keyHolder.getKey().longValue();
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Author(id, name);
        }
    }
}

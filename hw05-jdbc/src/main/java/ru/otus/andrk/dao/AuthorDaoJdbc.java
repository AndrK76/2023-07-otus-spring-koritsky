package ru.otus.andrk.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private static final String SELECT_PHRASE = "select id, name from authors";

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public List<Author> getAll() {
        return jdbc.query(SELECT_PHRASE, new AuthorMapper());
    }

    @Override
    public Author getById(long id) {
        try {
            return jdbc.queryForObject(SELECT_PHRASE + " where id=:id",
                    Map.of("id", id), new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void insert(Author author) {
        Map<String, Object> params = Map.of("id", author.id(), "name", author.name());
        jdbc.update("insert into authors (id,name) values (:id, :name)", params);
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

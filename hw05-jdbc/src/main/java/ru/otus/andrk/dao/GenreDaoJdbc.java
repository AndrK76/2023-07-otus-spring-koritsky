package ru.otus.andrk.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.andrk.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Repository
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcTemplate jdbc;

    private static final String SELECT_PHRASE = "select id, name from genres";

    @Override
    public List<Genre> getAll() {
        return jdbc.query(SELECT_PHRASE, new GenreMapper());
    }

    @Override
    public Genre getById(long id) {
        try {
            return jdbc.queryForObject(SELECT_PHRASE + " where id=:id",
                    Map.of("id", id), new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void insert(Genre genre) {
        Map<String, Object> params = Map.of("id", genre.id(), "name", genre.name());
        jdbc.update("insert into genres (id,name) values (:id, :name)", params);
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}

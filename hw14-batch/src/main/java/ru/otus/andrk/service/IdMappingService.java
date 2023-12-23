package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IdMappingService {

    private final JdbcTemplate jdbcTemplate;

    private final Map<String, Map<String, Long>> keyMap =
            Map.of("AUTHOR", new HashMap<>(),
                    "GENRE", new HashMap<>()
            );

    private final Map<String, String> entityTables =
            Map.of("AUTHOR", "authors",
                    "GENRE", "genres"
            );

    public void fillMap(String entityName) {
        jdbcTemplate
                .query(
                        String.format(
                                "Select id, mongo_id from %s where mongo_id is NOT null",
                                entityTables.get(entityName)),
                        (rs, rowNum) -> Pair.of(rs.getString("mongo_id"), rs.getLong("id")))
                .forEach(pair -> keyMap.get(entityName).put(pair.getFirst(), pair.getSecond()));
    }

    public long getKey(String entityName, String mongoId) {
        return keyMap.get(entityName).get(mongoId);
    }

}

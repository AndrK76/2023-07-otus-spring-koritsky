package ru.otus.andrk.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenreRdb {
    private long id;

    private String name;

    private String mongoId;

    public GenreRdb(String name, String mongoId) {
        this.name = name;
        this.mongoId = mongoId;
    }
}

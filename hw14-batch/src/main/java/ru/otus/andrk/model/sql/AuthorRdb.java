package ru.otus.andrk.model.sql;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorRdb {
    private long id;

    private String name;

    private String mongoId;

    public AuthorRdb(String name, String mongoId) {
        this.name = name;
        this.mongoId = mongoId;
    }
}

package ru.otus.andrk.model.sql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BookRdb {
    private long id;

    private String name;

    private String mongoId;

    private Long authorId;

    private Long genreId;

}

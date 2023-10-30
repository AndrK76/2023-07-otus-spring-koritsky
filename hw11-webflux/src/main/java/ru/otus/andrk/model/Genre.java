package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "genres")
public class Genre {
    @Id
    private String id;

    private String name;

    public Genre(String name) {
        this.name = name;
    }
}

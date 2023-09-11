package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Genre;

@Service
public class GenreToStringConverter implements Converter<Genre, String> {
    @Override
    public String convert(Genre genre) {
        return "Id: " + genre.getId() +
                "\tName: " + genre.getName();
    }
}

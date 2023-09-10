package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Genre;

@Component
public class GenreToStringConverter implements Converter<Genre, String> {
    @Override
    public String convert(Genre genre) {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(genre.getId())
                .append("\tName: ").append(genre.getName());
        return sb.toString();
    }
}

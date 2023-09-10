package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Author;

@Component
public class AuthorToStringConverter implements Converter<Author, String> {
    @Override
    public String convert(Author author) {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(author.id())
                .append("\tName: ").append(author.name());
        return sb.toString();
    }
}

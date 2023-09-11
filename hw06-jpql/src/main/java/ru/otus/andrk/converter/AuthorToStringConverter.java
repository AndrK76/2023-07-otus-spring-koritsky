package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Author;

@Service
public class AuthorToStringConverter implements Converter<Author, String> {
    @Override
    public String convert(Author author) {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(author.getId())
                .append("\tName: ").append(author.getName());
        return sb.toString();
    }
}

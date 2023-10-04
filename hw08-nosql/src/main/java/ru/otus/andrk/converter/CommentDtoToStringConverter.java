package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.CommentDto;

@Component
public class CommentDtoToStringConverter implements Converter<CommentDto, String> {
    @Override
    public String convert(CommentDto comment) {
        return "Id: " + comment.id() +
                "\tName: " + comment.text();
    }
}

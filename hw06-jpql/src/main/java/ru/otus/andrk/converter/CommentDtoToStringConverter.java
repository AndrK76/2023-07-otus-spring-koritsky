package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.CommentDto;

@Service
public class CommentDtoToStringConverter implements Converter<CommentDto, String> {
    @Override
    public String convert(CommentDto comment) {
        return "Id: " + comment.id() +
                "\tName: " + comment.text();
    }
}

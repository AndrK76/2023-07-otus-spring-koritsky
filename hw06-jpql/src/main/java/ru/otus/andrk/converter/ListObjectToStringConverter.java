package ru.otus.andrk.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListObjectToStringConverter implements Converter<List<? extends Object>, String> {

    private final Converter<BookDto, String> bookConverter;

    private final Converter<Author, String> authorConverter;

    private final Converter<Genre, String> genreConverter;

    private final Converter<CommentOnBookDto, String> commentBookConverter;

    private final Converter<CommentDto, String> commentConverter;

    @Override
    public String convert(List<? extends Object> elements) {
        StringBuilder sb = new StringBuilder();
        elements.forEach(r -> sb.append(convertToString(r)).append("\n"));
        return sb.toString();
    }

    String convertToString(Object obj) {
        if (obj.getClass().equals(Author.class)) {
            return authorConverter.convert((Author) obj);
        } else if (obj.getClass().equals(BookDto.class)) {
            return bookConverter.convert((BookDto) obj);
        } else if (obj.getClass().equals(Genre.class)) {
            return genreConverter.convert((Genre) obj);
        } else if (obj.getClass().equals(CommentDto.class)) {
            return commentConverter.convert((CommentDto) obj);
        } else if (obj.getClass().equals(CommentOnBookDto.class)) {
            return commentBookConverter.convert((CommentOnBookDto) obj);
        } else {
            return obj.toString();
        }
    }
}

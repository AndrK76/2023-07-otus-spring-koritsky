package ru.otus.andrk.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListObjectToStringConverter implements Converter<List<Object>, String> {

    private final Converter<Book, String> bookConverter;

    private final Converter<Author, String> authorConverter;

    private final Converter<Genre, String> genreConverter;

    @Override
    public String convert(List<Object> books) {
        StringBuilder sb = new StringBuilder();
        books.forEach(r -> sb.append(convertToString(r)).append("\n"));
        return sb.toString();
    }

    String convertToString(Object obj) {
        if (obj.getClass().equals(Author.class)) {
            return authorConverter.convert((Author) obj);
        } else if (obj.getClass().equals(Book.class)) {
            return bookConverter.convert((Book) obj);
        } else if (obj.getClass().equals(Genre.class)) {
            return genreConverter.convert((Genre) obj);
        } else {
            return obj.toString();
        }
    }
}

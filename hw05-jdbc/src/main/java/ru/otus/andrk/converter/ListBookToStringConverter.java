package ru.otus.andrk.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Book;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListBookToStringConverter implements Converter<List<Book>, String> {

    private final Converter<Book,String> bookConverter;

    @Override
    public String convert(List<Book> books) {
        StringBuilder sb = new StringBuilder();
        books.forEach(r -> sb.append(bookConverter.convert(r)).append("\n"));
        return sb.toString();
    }
}

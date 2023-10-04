package ru.otus.andrk.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.BookDto;

@Component
@RequiredArgsConstructor
public class BookDtoToStringConverter implements Converter<BookDto, String> {

    @Override
    public String convert(BookDto book) {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(book.getId())
                .append("\tName: ").append(book.getName());
        if (book.getAuthor() != null) {
            sb.append("\tAuthor: {Id: ").append(book.getAuthor().getId())
                    .append(", Name: ").append(book.getAuthor().getName())
                    .append("}");
        }
        if (book.getGenre() != null) {
            sb.append("\tGenre: {Id: ").append(book.getGenre().getId())
                    .append(", Name: ").append(book.getGenre().getName())
                    .append("}");
        }
        return sb.toString();
    }
}

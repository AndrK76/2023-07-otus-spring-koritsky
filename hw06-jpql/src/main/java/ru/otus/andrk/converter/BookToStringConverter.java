package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Book;

@Component
public class BookToStringConverter implements Converter<Book, String> {
    @Override
    public String convert(Book book) {
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

package ru.otus.andrk.converter;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.BookDto;

@Service
public class BookDtoToStringConverter implements Converter<BookDto, String> {
    private final ConversionService conversionService;

    public BookDtoToStringConverter(@Lazy ConversionService conversionService) {
        this.conversionService = conversionService;
    }

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
        if (book.getComments() != null && !book.getComments().isEmpty()) {
            sb.append("\n\tComments: [\n\t\t");
            var comments = conversionService.convert(book.getComments(), String.class)
                    .replace("\n", "\n\t\t");
            comments = comments.substring(0, comments.length() - "\n\t\t".length());
            sb.append(comments);
            sb.append("\n\t]");
        }
        return sb.toString();
    }
}

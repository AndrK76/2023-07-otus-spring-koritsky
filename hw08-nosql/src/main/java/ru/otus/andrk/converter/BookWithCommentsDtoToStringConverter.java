package ru.otus.andrk.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.BookWithCommentsDto;

@Component
@RequiredArgsConstructor
public class BookWithCommentsDtoToStringConverter implements Converter<BookWithCommentsDto, String> {

    private final CommentDtoToStringConverter commentConverter;

    @Override
    public String convert(BookWithCommentsDto book) {
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
            sb.append("\n\tComments: [");
            book.getComments().forEach(
                    r -> sb.append("\n\t\t")
                            .append(commentConverter.convert(r))
            );
            sb.append("\n\t]");
        }
        return sb.toString();
    }
}

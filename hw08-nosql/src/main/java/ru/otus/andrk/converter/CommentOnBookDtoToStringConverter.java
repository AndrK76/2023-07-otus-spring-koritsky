package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.CommentOnBookDto;

@Component
public class CommentOnBookDtoToStringConverter implements Converter<CommentOnBookDto, String> {
    @Override
    public String convert(CommentOnBookDto comment) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Id: ").append(comment.id())
                .append("\tName: ").append(comment.text())
                .append("\tBook: { Id:").append(comment.bookId())
                .append(", Name: ").append(comment.bookName());
        if (comment.authorName() != null) {
            sb.append(", Author: ").append(comment.authorName());
        }
        if (comment.genreName() != null) {
            sb.append(", Genre: ").append(comment.genreName());
        }
        sb.append("}");
        return sb.toString();
    }
}

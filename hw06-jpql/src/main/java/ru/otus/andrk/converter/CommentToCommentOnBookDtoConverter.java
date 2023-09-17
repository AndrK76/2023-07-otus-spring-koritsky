package ru.otus.andrk.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;

@Component
public class CommentToCommentOnBookDtoConverter implements Converter<Comment, CommentOnBookDto> {
    @Override
    public CommentOnBookDto convert(Comment comment) {
        Book book = comment.getBook();
        return new CommentOnBookDto(
                comment.getId(),
                comment.getText(),
                book.getId(),
                book.getName(),
                book.getAuthor() == null ? "" : book.getAuthor().getName(),
                book.getGenre() == null ? "" : book.getGenre().getName());
    }
}

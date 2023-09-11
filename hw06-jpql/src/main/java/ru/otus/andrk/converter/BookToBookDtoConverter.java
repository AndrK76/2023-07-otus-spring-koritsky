package ru.otus.andrk.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.model.Book;

@Component
@RequiredArgsConstructor
public class BookToBookDtoConverter implements Converter<Book, BookDto> {
    private final CommentToCommentDtoConverter commentConverter;

    @Override
    public BookDto convert(Book book) {
        var ret = BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .build();
        if (book.getComments() != null) {
            ret.setComments(book.getComments().stream().map(commentConverter::convert).toList());
        }
        return ret;
    }
}

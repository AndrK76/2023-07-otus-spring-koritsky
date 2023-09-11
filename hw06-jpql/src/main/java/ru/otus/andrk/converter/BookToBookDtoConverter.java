package ru.otus.andrk.converter;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.model.Book;

@Service
public class BookToBookDtoConverter implements Converter<Book, BookDto> {
    private final ConversionService conversionService;

    public BookToBookDtoConverter(@Lazy ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BookDto convert(Book book) {
        var ret = BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .build();
        if (book.getComments() != null) {
            ret.setComments(book.getComments().stream()
                    .map(r -> conversionService.convert(r, CommentDto.class)).toList());
        }
        return ret;
    }
}

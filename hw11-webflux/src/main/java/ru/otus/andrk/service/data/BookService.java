package ru.otus.andrk.service.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;

import java.util.List;

public interface BookService {
    Flux<BookDto> getAllBooks();

    Mono<BookDto> getBookById(long id);

    Mono<BookWithCommentsDto> getBookWithCommentsById(long id);

    Mono<BookDto> addBook(Mono<BookDto> book);

    Mono<BookDto> modifyBook(long bookId, Mono<BookDto> book);

    void deleteBook(long id);
}

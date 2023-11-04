package ru.otus.andrk.service.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.BookDto;

public interface BookService {
    Flux<BookDto> getAllBooks();

    Mono<Void> deleteBook(String id);

    Mono<BookDto> addBook(BookDto book);

    Mono<BookDto> modifyBook(String bookId, BookDto book);

}

package ru.otus.andrk.service.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.model.Book;

public interface BookService {
    Flux<BookDto> getAllBooks();

    Mono<Void> deleteBook(String id);

    Mono<BookDto> addBook(BookDto book);

    Mono<BookDto> modifyBook(String bookId, BookDto book);

    Mono<Book> getBook(String bookId);

}

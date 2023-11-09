package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.service.data.BookService;

import java.time.Duration;
import java.util.stream.IntStream;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final ControllerConfig config;

    @GetMapping("/api/v1/book")
    public Flux<BookDto> getAllBooks() {
        var srcData = config.isUseErrorSource()
                ? getFluxBooksWithError(config.getErrorMessageIndex())
                : bookService.getAllBooks();
        return srcData
                .delayElements(Duration.ofMillis(config.getListDelayInMs()), config.getScheduler());
    }

    @PostMapping("/api/v1/book")
    public Mono<BookDto> addBook(@RequestBody BookDto book) {
        return bookService.addBook(book);
    }

    @PutMapping("/api/v1/book/{id}")
    public Mono<BookDto> modifyBook(
            @PathVariable(name = "id") String bookId,
            @RequestBody BookDto book) {
        return bookService.modifyBook(bookId, book);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public Mono<Void> deleteBookBiId(@PathVariable(name = "id") String bookId) {
        return bookService.deleteBook(bookId);
    }

    //TODO: то что ниже - для иллюстрации вопроса и разумеется в контроллере ему не место
    public Flux<BookDto> getFluxBooksWithError(int exceptionPos) {
        var err = new OtherLibraryManipulationException(new RuntimeException());
        var src = IntStream.range(1, 15)
                .mapToObj(ind -> {
                    var book = new BookDto();
                    book.setId("q_" + ind);
                    book.setName("book " + ind);
                    return book;
                }).toList();
        return Flux.create(emitter -> {
            for (int i = 0; i < src.size(); i++) {
                if (i != exceptionPos) {
                    log.info("produce book {}", i);
                    emitter.next(src.get(i));
                } else {
                    log.info("produce error");
                    emitter.error(err);
                }
            }
        });
    }
}

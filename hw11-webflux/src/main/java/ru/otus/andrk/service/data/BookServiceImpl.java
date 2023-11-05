package ru.otus.andrk.service.data;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.config.DataLayerConfig;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.repository.BookRepository;

import java.time.Duration;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;

    private final DtoMapper mapper;

    private final DataLayerConfig config;

    private final CommentService commentService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    public Flux<BookDto> getAllBooks() {
        log.debug("call get all books");
        return bookRepo.findAll()
                .timeout(Duration.ofMillis(config.getWaitDataInMs()), config.getScheduler())
                .map(mapper::toDto)
                .doFirst(() -> log.debug("Start get all books"))
                .doOnNext(book -> log.debug("Get book {}", book.getId()))
                .doOnComplete(() -> log.debug("End get all books"))
                .onErrorMap(OtherLibraryManipulationException::new);
    }

    @Override
    //@Transactional
    public Mono<Void> deleteBook(String id) {
        return commentService.deleteAllCommentsForBook(id)
                .onErrorMap(OtherLibraryManipulationException::new)
                .then(Mono.just(id).publishOn(config.getScheduler())
                        .doOnNext(b -> bookRepo.deleteById(id))
                        .doOnNext(l -> log.debug("delete book id={}", l))
                ).then();
    }

    @Override
    //@Transactional
    public Mono<BookDto> addBook(BookDto book) {
        book.setId(null);
        return composeBook(book, null).publishOn(config.getScheduler())
                .flatMap(bookRepo::save).map(mapper::toDto)
                .doOnNext(b -> log.debug("add book id={}", b.getId()));
    }

    @Override
    @Transactional
    public Mono<BookDto> modifyBook(String bookId, BookDto book) {
        return composeBook(book, bookId).publishOn(config.getScheduler())
                .doOnNext(b -> log.debug("start modify book id={}", b.getId()))
                .flatMap(bookRepo::save).map(mapper::toDto)
                .doOnNext(b -> log.debug("end modify book id={}", b.getId()));
    }


    private Mono<Optional<AuthorDto>> actualizeAuthor(String authorName) {
        return Strings.isNullOrEmpty(authorName)
                ? Mono.just(Optional.empty())
                : authorService.getAuthorByName(authorName)
                .switchIfEmpty(authorService.addAuthor(authorName))
                .map(Optional::of);
    }

    private Mono<Optional<GenreDto>> actualizeGenre(String genreName) {
        return Strings.isNullOrEmpty(genreName)
                ? Mono.just(Optional.empty())
                : genreService.getGenreByName(genreName)
                .switchIfEmpty(genreService.addGenre(genreName))
                .map(Optional::of);
    }

    private Mono<Book> composeBook(BookDto book, String bookId) {
        return Mono.zip(
                        Mono.just(book),
                        actualizeAuthor(book.getAuthorName()),
                        actualizeGenre(book.getGenreName())
                )
                .flatMap(data -> {
                    var author = data.getT2().map(mapper::fromDto).orElse(null);
                    var genre = data.getT3().map(mapper::fromDto).orElse(null);
                    var ret = new Book(bookId, data.getT1().getName(), author, genre);
                    return Mono.just(ret);
                });
    }


}

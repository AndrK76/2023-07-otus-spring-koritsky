package ru.otus.andrk.service.data;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.andrk.config.LibraryConfig;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.repository.BookRepository;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;

    private final DtoMapper mapper;
    private final Scheduler scheduler;
    private final LibraryConfig config;

    private final CommentService commentService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    public Flux<BookDto> getAllBooks() {
        log.debug("call get all books");
        return bookRepo.findAll()
                .timeout(Duration.ofMillis(config.getWaitDataInMs()), scheduler)
                .delayElements(Duration.ofMillis(config.getListDelayInMs()), scheduler)
                .map(mapper::toDto)
                .doFirst(() -> log.debug("Start get all books"))
                .doOnNext(book -> log.debug("Get book {}", book.getId()))
                .doOnComplete(() -> log.debug("End get all books"));
    }

    @Override
    @Transactional
    public Mono<Void> deleteBook(String id) {
        return commentService.deleteAllCommentsForBook(id)
                .then(Mono.just(id).publishOn(scheduler)
                        .doOnNext(b -> bookRepo.deleteById(id))
                        .doOnNext(l -> log.debug("delete book id={}", l))
                ).then();
    }

    @Override
    public Mono<BookDto> getBookById(long id) {
        return null;
    }

    @Override
    @Transactional
    public Mono<BookDto> addBook(BookDto book) {
        return composeBook(book).publishOn(scheduler)
                .flatMap(bookRepo::save).map(mapper::toDto)
                .doOnNext(b -> log.debug("add book id={}", b.getId()));
    }

    @Override
    @Transactional
    public Mono<BookDto> modifyBook(String bookId, BookDto book) {
        book.setId(bookId);
        return composeBook(book).publishOn(scheduler)
                .doOnNext(b -> log.debug("start modify book id={}", b.getId()))
                .flatMap(bookRepo::save).map(mapper::toDto)
                .doOnNext(b -> log.debug("end modify book id={}", b.getId()));
    }


    private Mono<AuthorDto> actualizeAuthor(String authorName) {
        return authorService.getAuthorByName(authorName)
                .switchIfEmpty(authorService.addAuthor(authorName));
    }

    private Mono<GenreDto> actualizeGenre(String genreName) {
        return genreService.getGenreByName(genreName)
                .switchIfEmpty(genreService.addGenre(genreName));
    }

    private Mono<Book> composeBook(BookDto book) {
        return Mono.zip(
                        Mono.just(book),
                        Strings.isNullOrEmpty(book.getAuthorName())
                                ? Mono.empty()
                                : actualizeAuthor(book.getAuthorName()),
                        Strings.isNullOrEmpty(book.getGenreName())
                                ? Mono.empty()
                                : actualizeGenre(book.getGenreName()))
                .flatMap(data -> {
                    var author = data.getT2() == null ? null : mapper.fromDto(data.getT2());
                    var genre = data.getT3() == null ? null : mapper.fromDto(data.getT3());
                    var ret = new Book(data.getT1().getId(), data.getT1().getName(), author, genre);
                    return Mono.just(ret);
                });
    }


}

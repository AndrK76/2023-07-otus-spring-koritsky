package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.DefaultServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.BookService;

import java.util.Locale;

@RestController
@Log4j2
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    private final LocaleContextResolver localeResolver;


    @GetMapping("/api/v1/book")
    public Flux<BookDto> getAllBooks(){
        return bookService.getAllBooks();
    }


    @GetMapping("/api/v1/author")
    public Flux<AuthorDto> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @GetMapping("/locale")
    public Mono<String> getLang(ServerWebExchange exchange){
        //localeResolver.resolveLocaleContext(exchange)
        return Mono.just(localeResolver.resolveLocaleContext(exchange).getLocale().getLanguage());
    }


}

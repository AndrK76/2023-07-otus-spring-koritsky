package ru.otus.andrk.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.GenreService;

@RestController
@Log4j2
@RequiredArgsConstructor
public class OtherDataController {
    private final AuthorService authorService;

    private final GenreService genreService;


    @GetMapping("/api/v1/author")
    public Flux<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/api/v1/genre")
    public Flux<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }
}

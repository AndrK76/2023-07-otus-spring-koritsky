package ru.otus.andrk.service.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.andrk.dto.GenreDto;

import java.util.List;

public interface GenreService {
    Flux<GenreDto> getAllGenres();

    Mono<GenreDto> addGenre(String genreName);

    Mono<GenreDto> getGenreByName(String name);
}

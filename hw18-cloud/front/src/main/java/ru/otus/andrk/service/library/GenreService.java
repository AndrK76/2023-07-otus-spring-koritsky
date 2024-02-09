package ru.otus.andrk.service.library;

import org.springframework.http.ResponseEntity;

public interface GenreService {
    ResponseEntity<?> getGenres();
}

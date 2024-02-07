package ru.otus.andrk.service.library;

import org.springframework.http.ResponseEntity;

public interface AuthorService {
    ResponseEntity<?> getAuthors();
}

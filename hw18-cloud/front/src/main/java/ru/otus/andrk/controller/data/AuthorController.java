package ru.otus.andrk.controller.data;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.service.library.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/api/v1/author")
    public ResponseEntity<?> getAuthors() {
        return authorService.getAuthors();
    }
}

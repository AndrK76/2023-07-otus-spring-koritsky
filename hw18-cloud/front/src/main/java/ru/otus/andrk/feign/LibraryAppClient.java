package ru.otus.andrk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.GenreDto;

import java.util.List;

@FeignClient(name = "${app.backend.app-name}", configuration = {LibraryAppClientErrorDecoder.class})
public interface LibraryAppClient {

    @GetMapping("/api/v1/book")
    List<BookDto> getAllBooks();

    @GetMapping("/api/v1/book/{book}/comment")
    List<CommentDto> getCommentsForBook(@PathVariable(name = "book") long bookId);

    @GetMapping("/api/v1/author")
    List<AuthorDto> getAuthors();

    @GetMapping("/api/v1/genre")
    List<GenreDto> getGenres();

    @PostMapping(value = "/api/v1/validation/book")
    BookDto validateBook(@RequestBody BookDto book);

    @PostMapping(value = "/api/v1/validation/comment")
    CommentDto validateComment(@RequestBody CommentDto comment);

    @PostMapping("/api/v1/book")
    BookDto addBook(@RequestBody BookDto book);

    @PutMapping("/api/v1/book/{book}")
    BookDto modifyBook(@PathVariable(name = "book") long bookId,
                       @RequestBody BookDto book);

    @DeleteMapping("/api/v1/book/{book}")
    long deleteBook(@PathVariable(name = "book") long bookId);

    @PostMapping("/api/v1/book/{book}/comment")
    CommentDto addComment(@PathVariable(name = "book") long bookId,
                          @RequestBody CommentDto comment);

    @PutMapping("/api/v1/comment/{comment}")
    CommentDto modifyComment(@PathVariable(name = "comment") long commentId,
                             @RequestBody CommentDto comment);

    @DeleteMapping("/api/v1/comment/{comment}")
    long deleteComment(@PathVariable(name = "comment") long commentId);

}

package ru.otus.andrk.controller;

import com.google.common.base.Strings;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.service.library.AuthorService;
import ru.otus.andrk.service.library.BookService;
import ru.otus.andrk.service.library.GenreService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BookController {
    private static final String ACTION_ADD = "add";

    private static final String ACTION_EDIT = "edit";

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping({"/book"})
    public String index(Model model) {
        var books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book_list";
    }

    @GetMapping("/book/add")
    public String addBook(Model model) {
        addBookDataToModel(model, ACTION_ADD, new BookDto());
        return "book_edit";
    }

    @PostMapping("/book/add")
    public String addBook(@Valid @ModelAttribute("book") BookDto book,
                          BindingResult bindingResult,
                          Model model) {
        return processAddOrModifyBook(ACTION_ADD, book, bindingResult, model);
    }

    @GetMapping("/book/edit")
    public String editBook(@RequestParam(name = "id") long bookId, Model model) {
        var book = Optional.ofNullable(bookService.getBookById(bookId))
                .orElseThrow(NoExistBookException::new);
        addBookDataToModel(model, ACTION_EDIT, book);
        return "book_edit";
    }

    @PostMapping("/book/edit")
    public String editBook(@Valid @ModelAttribute("book") BookDto book,
                           BindingResult bindingResult,
                           Model model) {
        return processAddOrModifyBook(ACTION_EDIT, book, bindingResult, model);
    }

    @GetMapping("/book/delete")
    public String deleteBook(@RequestParam(name = "id") long bookId, Model model) {
        model.addAttribute("backUrl", "/book");
        model.addAttribute("acceptUrl", "/book/delete");

        var books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("delete", bookId);
        return "book_list";
    }

    @PostMapping("/book/delete")
    public String deleteBook(@RequestParam(name = "id") long bookId) {
        log.debug("delete book id={}", bookId);
        bookService.deleteBook(bookId);
        return "redirect:/book";
    }

    private void addBookDataToModel(Model model, String action, BookDto book) {
        model.addAttribute("action", action);
        model.addAttribute("title", "book." + action + "-title");
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("genres", genreService.getAllGenres());
    }

    private String processAddOrModifyBook(String action,
                                          BookDto book, BindingResult bindingResult, Model model) {
        log.debug("{} {}", action, book);
        if (bindingResult.hasErrors()) {
            addBookDataToModel(model, action, book);
            return "book_edit";
        }
        checkAuthorAndAddNewIfNecessary(book);
        checkGenreAndAddNewIfNecessary(book);

        if (action.equals(ACTION_ADD)) {
            bookService.addBook(book.getName(), book.getAuthorId(), book.getGenreId());
        } else {
            bookService.modifyBook(book.getId(), book.getName(), book.getAuthorId(), book.getGenreId());
        }
        return "redirect:/book";
    }

    private void checkAuthorAndAddNewIfNecessary(BookDto book) {


        if (!Strings.isNullOrEmpty(book.getAuthorName())) {
            var author = authorService.getAuthorByName(book.getAuthorName());
            if (author == null) {
                author = authorService.addAuthor(book.getAuthorName());
            }
            book.setAuthorId(author.id());
        } else {
            book.setAuthorId(null);
        }
    }

    private void checkGenreAndAddNewIfNecessary(BookDto book) {
        if (!Strings.isNullOrEmpty(book.getGenreName())) {
            var genre = genreService.getGenreByName(book.getGenreName());
            if (genre == null) {
                genre = genreService.addGenre(book.getGenreName());
            }
            book.setGenreId(genre.id());
        } else {
            book.setGenreId(null);
        }
    }
}

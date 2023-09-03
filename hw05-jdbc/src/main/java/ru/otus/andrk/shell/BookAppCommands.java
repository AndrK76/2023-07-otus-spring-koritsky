package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.andrk.service.LibraryService;

@ShellComponent
@RequiredArgsConstructor
public class BookAppCommands {
    private final LibraryService libraryService;

    private final ConversionService conversionService;

    @ShellMethod(value = "List all books", key = {"list all books", "all books", "all", "list"})
    public String getAllBooks() {
        var books = libraryService.getAllBooks();
        StringBuilder sb = new StringBuilder();
        books.forEach(r->sb.append(conversionService.convert(r,String.class)).append("\n"));
        return sb.toString();
    }
}

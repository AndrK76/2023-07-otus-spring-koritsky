package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.andrk.excepton.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.service.AuthorService;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCommands {
    private final AuthorService authorService;

    private final ConversionService conversionService;

    @ShellMethod(
            value = "List all authors",
            key = {"list all authors", "all authors", "list authors", "authors", "list author"})
    public String getAllAuthors() {
        try {
            var allAuthors = authorService.getAllAuthors();
            return conversionService.convert(allAuthors, String.class);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't get author list from library, see log for detail";
        }
    }

    @ShellMethod(value = "Add new author", key = {"add author", "new author"})
    public String addAuthor(
            String authorName
    ) {
        try {
            Author author = authorService.addAuthor(authorName);
            return conversionService.convert(author, String.class);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't add author to library, see log for detail";
        }
    }
}

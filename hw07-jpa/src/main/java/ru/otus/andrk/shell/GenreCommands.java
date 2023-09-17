package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.andrk.converter.ListToStringConversionService;
import ru.otus.andrk.excepton.OtherLibraryManipulationException;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.service.GenreService;

@ShellComponent
@RequiredArgsConstructor
public class GenreCommands {
    private final GenreService genreService;

    private final ConversionService conversionService;

    private final ListToStringConversionService listToStringConversionService;

    @ShellMethod(
            value = "List all genres",
            key = {"list all genres", "all genres", "list genres", "genres", "list genre"})
    public String getAllGenres() {
        try {
            var allGenres = genreService.getAllGenres();
            return listToStringConversionService.convert(allGenres);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't get genre list from library, see log for detail";
        }
    }

    @ShellMethod(value = "Add new genre", key = {"add genre", "new genre"})
    public String addGenre(
            String genreName
    ) {
        try {
            Genre genre = genreService.addGenre(genreName);
            return conversionService.convert(genre, String.class);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't add genre to library, see log for detail";
        }
    }
}

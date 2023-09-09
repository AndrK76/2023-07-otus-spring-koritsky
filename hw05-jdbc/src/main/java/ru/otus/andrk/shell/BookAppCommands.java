package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.andrk.exception.NoExistAuthorException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistGenreException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.service.AuthorService;
import ru.otus.andrk.service.BookService;
import ru.otus.andrk.service.GenreService;

@ShellComponent
@RequiredArgsConstructor
public class BookAppCommands {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final ConversionService conversionService;

    @ShellMethod(value = "List all books", key = {"list all books", "all books", "all", "list"})
    public String getAllBooks() {
        try {
            var allBooks = bookService.getAllBooks();
            return conversionService.convert(allBooks, String.class);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't get book list from library, see log for detail";
        }
    }

    @ShellMethod(value = "Get book by id", key = {"get book", "book", "get"})
    public String getBookById(@ShellOption(help = "Book Id") long bookId) {
        try {
            var book = bookService.getBookById(bookId);
            return conversionService.convert(book, String.class);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't get book by id from library, see log for detail";
        }
    }

    @ShellMethod(value = "Add new book", key = {"add book", "add", "new"})
    public String addBook(
            @ShellOption(help = "Book Name") String bookName,
            @ShellOption(help = "Author Id", defaultValue = "") Long authorId,
            @ShellOption(help = "Genre Id", defaultValue = "") Long genreId) {
        try {
            var author = authorId == null ? null : authorService.getAuthorById(authorId);
            var genre = genreId == null ? null : genreService.getGenreById(genreId);
            var storedBook = bookService.addBook(bookName, author, genre);
            return "Book added to library\n" + conversionService.convert(storedBook, String.class);
        } catch (NoExistAuthorException e) {
            return "Error, Author with id=" + authorId + " not exist in library";
        } catch (NoExistGenreException e) {
            return "Error, Genre with id=" + genreId + " not exist in library";
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't add book to library, see log for detail";
        }
    }

    @ShellMethod(value = "Modify exist book", key = {"modify book", "modify", "update"})
    public String modifyBook(
            @ShellOption(help = "Book Id") long bookId,
            @ShellOption(help = "Book Name", defaultValue = "") String bookName,
            @ShellOption(help = "Author Id", defaultValue = "") Long authorId,
            @ShellOption(help = "Genre Id", defaultValue = "") Long genreId) {
        var author = authorId == null ? null : authorService.getAuthorById(authorId);
        var genre = genreId == null ? null : genreService.getGenreById(genreId);
        try {
            var storedBook = bookService.modifyBook(bookId, bookName, author, genre);
            return "Book changed, new state\n"
                    + conversionService.convert(storedBook, String.class);
        } catch (NoExistBookException e) {
            return "Error, Book with id=" + bookId + " not exist in library";
        } catch (NoExistAuthorException e) {
            return "Error, Author with id=" + authorId + " not exist in library";
        } catch (NoExistGenreException e) {
            return "Error, Genre with id=" + genreId + " not exist in library";
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't add book to library, see log for detail";
        }
    }

    @ShellMethod(value = "Delete book by id", key = {"delete book", "delete", "remove"})
    public String deleteBookById(
            @ShellOption(help = "Book Id") long bookId
    ) {
        try {
            bookService.deleteBook(bookId);
            return "Book with id=" + bookId + " removed from library";
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't remove book to library, see log for detail";
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

    @ShellMethod(
            value = "List all genres",
            key = {"list all genres", "all genres", "list genres", "genres", "list genre"})
    public String getAllGenres() {
        try {
            var allGenres = genreService.getAllGenres();
            return conversionService.convert(allGenres, String.class);
        } catch (OtherLibraryManipulationException e) {
            return "Error, can't get genre list from library, see log for detail";
        }
    }

}

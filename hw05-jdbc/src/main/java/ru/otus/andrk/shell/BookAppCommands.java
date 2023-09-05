package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.service.AddAlreadyExistBookException;
import ru.otus.andrk.service.LibraryService;
import ru.otus.andrk.service.ModifyNoExistBookException;
import ru.otus.andrk.service.OtherBookManipulationException;

@ShellComponent
@RequiredArgsConstructor
public class BookAppCommands {
    private final LibraryService libraryService;

    private final ConversionService conversionService;

    @ShellMethod(value = "List all books", key = {"list all books", "all books", "all", "list"})
    public String getAllBooks() {
        try {
            var allBooks = libraryService.getAllBooks();
            return conversionService.convert(allBooks, String.class);
        } catch (OtherBookManipulationException e) {
            return "Error, can't get book list from library, see log for detail";
        }
    }

    @ShellMethod(value = "Get book by id", key = {"get book", "book", "get"})
    public String getBookById(@ShellOption(help = "Book Id") long bookId) {
        try {
            var book = libraryService.getBookById(bookId);
            return conversionService.convert(book, String.class);
        } catch (OtherBookManipulationException e) {
            return "Error, can't get book by id from library, see log for detail";
        }
    }

    @ShellMethod(value = "Add new book", key = {"add book", "add", "new"})
    public String addBook(
            @ShellOption(help = "Book Id") long bookId,
            @ShellOption(help = "Book Name") String bookName,
            @ShellOption(help = "Author Id", defaultValue = "") Long authorId,
            @ShellOption(help = "Author Name", defaultValue = "") String authorName,
            @ShellOption(help = "Genre Id", defaultValue = "") Long genreId,
            @ShellOption(help = "Genre Name", defaultValue = "") String genreName) {
        Book book = new Book(bookId, bookName);
        if (authorId != null) {
            book.setAuthor(new Author(authorId, authorName));
        }
        if (genreId != null) {
            book.setGenre(new Genre(genreId, genreName));
        }
        try {
            var storedBook = libraryService.addBook(book);
            return "Book added to library\n" + conversionService.convert(storedBook, String.class);
        } catch (AddAlreadyExistBookException e) {
            return "Error, Book with id=" + bookId + " already exist in library";
        } catch (OtherBookManipulationException e) {
            return "Error, can't add book to library, see log for detail";
        }
    }

    @ShellMethod(value = "Modify exist book", key = {"modify book", "modify", "update"})
    public String modifyBook(
            @ShellOption(help = "Book Id") long bookId,
            @ShellOption(help = "Book Name", defaultValue = "") String bookName,
            @ShellOption(help = "Author Id", defaultValue = "") Long authorId,
            @ShellOption(help = "Author Name", defaultValue = "") String authorName,
            @ShellOption(help = "Genre Id", defaultValue = "") Long genreId,
            @ShellOption(help = "Genre Name", defaultValue = "") String genreName) {
        Book book = new Book(bookId, bookName);
        if (authorId != null) {
            book.setAuthor(new Author(authorId, authorName));
        }
        if (genreId != null) {
            book.setGenre(new Genre(genreId, genreName));
        }
        try {
            var storedBook = libraryService.modifyBook(book);
            return "Book changed, new state\n"
                    + conversionService.convert(storedBook, String.class);
        } catch (ModifyNoExistBookException e) {
            return "Error, Book with id=" + bookId + " not exist in library";
        } catch (OtherBookManipulationException e) {
            return "Error, can't add book to library, see log for detail";
        }
    }

    @ShellMethod(value = "Delete book by id", key = {"delete book", "delete", "remove"})
    public String deleteBookById(
            @ShellOption(help = "Book Id") long bookId
    ) {
        try {
            libraryService.deleteBook(bookId);
            return "Book with id=" + bookId + " removed from library";
        } catch (OtherBookManipulationException e) {
            return "Error, can't add book to library, see log for detail";
        }
    }

    @ShellMethod(value = "List all authors", key = {"list all authors", "all authors", "list authors", "authors"})
    public String getAllAuthors() {
        try {
            var allAuthors = libraryService.getAllAuthors();
            return conversionService.convert(allAuthors, String.class);
        } catch (OtherBookManipulationException e) {
            return "Error, can't get book list from library, see log for detail";
        }
    }

    @ShellMethod(value = "List all genres", key = {"list all genres", "all genres", "list genres", "genres"})
    public String getAllGenres() {
        try {
            var allGenres = libraryService.getAllGenres();
            return conversionService.convert(allGenres, String.class);
        } catch (OtherBookManipulationException e) {
            return "Error, can't get book list from library, see log for detail";
        }
    }

}

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
import ru.otus.andrk.service.LibraryManipulationResult;
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
        var result = libraryService.getAllBooks();
        return parseManipulationResult(result);
    }

    @ShellMethod(value = "Get book by id", key = {"get book", "book", "get"})
    public String getBookById(@ShellOption(help = "Book Id") long bookId) {
        var result = libraryService.getBookById(bookId);
        return parseManipulationResult(result);
    }

    @ShellMethod(value = "Add new book", key = {"add book", "add", "new"})
    public String addBook(
            @ShellOption(help = "Book Id") long bookId,
            @ShellOption(help = "Book Name") String bookName,
            @ShellOption(help = "Author Id", defaultValue = "") Long authorId,
            @ShellOption(help = "Author Name", defaultValue = "") String authorName,
            @ShellOption(help = "Genre Id", defaultValue = "") Long genreId,
            @ShellOption(help = "Genre Name", defaultValue = "") String genreName
    ) {
        Book book = new Book(bookId, bookName);
        if (authorId != null) {
            book.setAuthor(new Author(authorId, authorName));
        }
        if (genreId != null) {
            book.setGenre(new Genre(genreId, genreName));
        }
        try {
            libraryService.addBook(book);
            return "Book added to library\n" + conversionService.convert(book, String.class);
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
            @ShellOption(help = "Genre Name", defaultValue = "") String genreName
    ) {
        Book book = new Book(bookId, bookName);
        if (authorId != null) {
            book.setAuthor(new Author(authorId, authorName));
        }
        if (genreId != null) {
            book.setGenre(new Genre(genreId, genreName));
        }
        try {
            libraryService.modifyBook(book);
            //book = libraryService.getBookById(book.getId());
            return "Book changed, new state\n"
                    //+ conversionService.convert(book, String.class)
                    ;
        } catch (ModifyNoExistBookException e) {
            return "Error, Book with id=" + bookId + " not exist in library";
        } catch (OtherBookManipulationException e) {
            return "Error, can't add book to library, see log for detail";
        }
    }

    private String parseManipulationResult(LibraryManipulationResult result) {
        StringBuilder sb = new StringBuilder();
        String rowSplitter = "";
        if (!result.isSuccess()) {
            sb.append("Error, ");
        }
        if (result.message() != null) {
            sb.append(result.message());
            rowSplitter = "\n";
        }
        if (result.data() != null) {
            sb.append(rowSplitter);
            sb.append(conversionService.convert(result.data(), String.class));
        }
        return sb.toString();
    }
}

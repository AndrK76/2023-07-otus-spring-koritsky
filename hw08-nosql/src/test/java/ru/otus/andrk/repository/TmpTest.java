package ru.otus.andrk.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.service.AuthorService;
import ru.otus.andrk.service.BookService;
import ru.otus.andrk.service.GenreService;

@DataMongoTest
@ComponentScan({"ru.otus.andrk.repository", "ru.otus.andrk.service",
        "ru.otus.andrk.config", "ru.otus.andrk.converter"})
public class TmpTest {

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private GenreRepository genreRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CommentRepository commentRepo;

    @Test
    public void test() {
        authorRepo.insertAuthor(new Author("Author 3"));
        authorRepo.insertAuthor(new Author("Author 4"));

        genreRepo.insertGenre(new Genre("Genre 3"));
        genreRepo.insertGenre(new Genre("Genre 4"));

        bookRepo.insertBook(new Book("Книга 99",
                authorRepo.findById(3L).get(),
                null
        ));

        bookRepo.insertBook(new Book("Книга 999",
                authorRepo.findById(2L).get(),
                genreRepo.findById(1L).get()
        ));

        System.out.println(authorRepo.findById(1L));
        System.out.println(genreRepo.findById(3L));
        var book = bookRepo.findById(2L).get();
        System.out.println(book);
        System.out.println("\t" + book.getAuthor());
        System.out.println("\t" + book.getGenre());
        book = bookRepo.findById(6L).get();
        System.out.println(book);
        System.out.println("\t" + book.getAuthor());
        System.out.println("\t" + book.getGenre());

    }

    @Test
    public void test2() {
        System.out.println("\nAuthor");
        System.out.println(authorService.getAuthorById(1L));
        System.out.println("insert");
        System.out.println(authorService.addAuthor("Иванов"));
        System.out.println("All");
        authorService.getAllAuthors().forEach(System.out::println);

        System.out.println("\nGenre");
        System.out.println(genreService.getGenreById(1L));
        System.out.println("insert");
        System.out.println(genreService.addGenre("Стихи"));
        System.out.println("All");
        genreService.getAllGenres().forEach(System.out::println);

        System.out.println("\nBook");
        System.out.println(bookService.getBookById(2L));
        System.out.println("insert");
        System.out.println(bookService.addBook("Новая", 3L, 3L));
        System.out.println("All");
        bookService.getAllBooks().forEach(System.out::println);
        System.out.println("Update");
        System.out.println(
                bookService.modifyBook(2L, "Книга 2", null, 3L));
        System.out.println("Delete");
        bookService.deleteBook(1L);
        System.out.println("1=" + bookService.getBookById(1L));

    }

    @Test
    public void test3() {
        var book = bookRepo.findById(1L).get();
        System.out.println(book);
        var comment = commentRepo.insertComment(new Comment("текст коммента", book));
        System.out.println(comment);
        book = comment.getBook();
        System.out.println("new:" + book);

        comment = commentRepo.findById(3L).get();
        book = comment.getBook();
        System.out.println(comment);
        System.out.println("new:" + book);

        comment = commentRepo.findById(7L).get();
        book = comment.getBook();
        System.out.println(comment);
        System.out.println("new:" + book);

    }
}

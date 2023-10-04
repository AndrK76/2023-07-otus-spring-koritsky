package ru.otus.andrk.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.AuthorRepository;
import ru.otus.andrk.repository.BookRepository;
import ru.otus.andrk.repository.CommentRepository;
import ru.otus.andrk.repository.GenreRepository;

import java.util.HashMap;
import java.util.Map;

@ChangeLog(order = "001")
public class InitDatabase {

    private Map<String, Genre> genres = new HashMap<>(
            Map.of(
                    "noGenre", new Genre("Не указан"),
                    "stih", new Genre("Стихи"),
                    "proza", new Genre("Проза")));

    private Map<String, Author> authors = new HashMap<>(Map.of(
            "pushkin", new Author("А.С. Пушкин"),
            "knuth", new Author("D.E. Knuth")));

    @ChangeSet(order = "000", id = "dropDB", author = "AndrK", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenresAndAuthors", author = "AndrK")
    public void initGenresAndAuthors(GenreRepository genreRepo, AuthorRepository authorRepo) {
        for (var key : genres.keySet()) {
            genres.put(key, genreRepo.insertGenre(genres.get(key)));
        }
        for (var key : authors.keySet()) {
            authors.put(key, authorRepo.insertAuthor(authors.get(key)));
        }
    }

    @ChangeSet(order = "002", id = "initBooksAndComments", author = "AndrK")
    public void initBooksAndComments(BookRepository bookRepo, CommentRepository commentRepo) {
        var bookOnegin = bookRepo.insertBook(
                new Book("Евгений Онегин", authors.get("pushkin"), genres.get("stih"))
        );
        var bookKnuth = bookRepo.insertBook(
                new Book("The Art of Computer Programming",
                        authors.get("knuth"), genres.get("noGenre"))
        );
        commentRepo.insertComment(new Comment("Клёва", bookKnuth));
        commentRepo.insertComment(new Comment("Нудно", bookKnuth));
    }


}

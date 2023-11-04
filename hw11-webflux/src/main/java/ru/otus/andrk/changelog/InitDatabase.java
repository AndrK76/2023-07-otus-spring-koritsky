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
            "knuth", new Author("D.E. Knuth"),
            "orlovsky", new Author("Г.Ю. Орловский")
    ));

    @ChangeSet(order = "000", id = "dropDB", author = "AndrK", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenresAndAuthors", author = "AndrK")
    public void initGenresAndAuthors(GenreRepository genreRepo, AuthorRepository authorRepo) {
        for (var key : genres.keySet()) {
            genres.put(key, genreRepo.save(genres.get(key)).block());
        }
        for (var key : authors.keySet()) {
            authors.put(key, authorRepo.save(authors.get(key)).block());
        }
    }

    @ChangeSet(order = "002", id = "initBooksAndComments", author = "AndrK")
    public void initBooksAndComments(BookRepository bookRepo, CommentRepository commentRepo) {
        var bookOnegin = bookRepo.save(
                new Book("Евгений Онегин", authors.get("pushkin"), genres.get("stih"))
        ).block();
        var bookKnuth = bookRepo.save(
                new Book("The Art of Computer Programming",
                        authors.get("knuth"), genres.get("noGenre"))
        ).block();

        commentRepo.save(new Comment(
                "Роман Евгений Онегин на мой взгляд должен прочитать каждый! Интересно наблюдать за размышлениями героев!\n" +
                        "Проследить безответную любовь, которую уже не получится вернуть. Обожаю письма от Онегина и Татьяны,\n" +
                        "они наполнены чувствами и пропитаны смыслом, правда для каждого своим! Несмотря на то, что роман написан \n" +
                        "был очень давно, многие мысли актуальны и по сей день. Тем кто не читал-желаю этого, а те кто читал– советую\n" +
                        "ещё раз. Такие произведения никогда не стареют!\n" +
                        "https://www.litres.ru/book/aleksandr-pushkin/evgeniy-onegin-171966/otzivi/", bookOnegin)).block();
        commentRepo.save(new Comment("Аффтар пеши исчо", bookOnegin)).block();
        commentRepo.save(new Comment("Клёва", bookKnuth)).block();
        commentRepo.save(new Comment("Нудно", bookKnuth)).block();

        for (int i = 1; i < 15; i++) {
            var book = new Book("Ричард Длинные руки. Опус " + i,
                    authors.get("orlovsky"), genres.get("proza"));
            bookRepo.save(book).block();
            var couComment = (int) (Math.random() * 20);
            for (int j = 0; j < couComment; j++) {
                commentRepo.save(new Comment("Комментарий №" + j, book)).block();
            }
        }

    }


}

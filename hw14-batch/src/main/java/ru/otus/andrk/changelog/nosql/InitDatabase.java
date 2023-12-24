package ru.otus.andrk.changelog.nosql;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.andrk.model.nosql.AuthorMongo;
import ru.otus.andrk.model.nosql.BookMongo;
import ru.otus.andrk.model.nosql.CommentMongo;
import ru.otus.andrk.model.nosql.GenreMongo;

import java.util.HashMap;
import java.util.Map;

@ChangeLog(order = "001")
@SuppressWarnings("unused")
public class InitDatabase {

    private final Map<String, GenreMongo> genres = new HashMap<>(
            Map.of(
                    "noGenre", new GenreMongo("Не указан"),
                    "stih", new GenreMongo("Стихи"),
                    "proza", new GenreMongo("Проза")));

    private final Map<String, AuthorMongo> authors = new HashMap<>(Map.of(
            "pushkin", new AuthorMongo("А.С. Пушкин"),
            "knuth", new AuthorMongo("D.E. Knuth"),
            "orlovsky", new AuthorMongo("Г.Ю. Орловский")
    ));

    @ChangeSet(order = "000", id = "dropDB", author = "AndrK", runAlways = true)
    public void dropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "initGenresAndAuthors", author = "AndrK")
    public void initGenresAndAuthors(MongockTemplate template) {
        genres.replaceAll((k, v) -> template.save(genres.get(k)));
        authors.replaceAll((k, v) -> template.save(authors.get(k)));
    }


    @ChangeSet(order = "002", id = "initBooksAndComments", author = "AndrK")
    public void initBooksAndComments(MongockTemplate template) {
        initMainBooks(template);
        initAdditionalBooks(template, 15, 18);
    }

    private void initMainBooks(MongoOperations template) {
        var bookOnegin = template.save(
                new BookMongo("Евгений Онегин", authors.get("pushkin"), genres.get("stih"))
        );
        var bookKnuth = template.save(
                new BookMongo("The Art of Computer Programming",
                        authors.get("knuth"), genres.get("noGenre"))
        );

        template.save(new CommentMongo(
                "Роман Евгений Онегин на мой взгляд должен прочитать каждый!" +
                        " Интересно наблюдать за размышлениями героев!\n" +
                        "Проследить безответную любовь, которую уже не получится вернуть." +
                        " Обожаю письма от Онегина и Татьяны,\n" +
                        "они наполнены чувствами и пропитаны смыслом, правда для каждого своим!" +
                        " Несмотря на то, что роман написан \n" +
                        "был очень давно, многие мысли актуальны и по сей день. Тем кто не " +
                        "читал-желаю этого, а те кто читал– советую\n" +
                        "ещё раз. Такие произведения никогда не стареют!\n" +
                        "https://www.litres.ru/book/aleksandr-pushkin/evgeniy-onegin-171966/otzivi/",
                bookOnegin));
        template.save(new CommentMongo("Аффтар пеши исчо", bookOnegin));
        template.save(new CommentMongo("Клёва", bookKnuth));
        template.save(new CommentMongo("Нудно", bookKnuth));
    }

    private void initAdditionalBooks(MongoOperations template, int countBook, int countCommentMax) {
        for (int i = 1; i < countBook; i++) {
            var book = new BookMongo("Ричард Длинные руки. Опус " + i,
                    authors.get("orlovsky"), genres.get("proza"));
            template.save(book);
            var couComment = (int) (Math.random() * countCommentMax);
            for (int j = 0; j < couComment; j++) {
                template.save(new CommentMongo("Комментарий №" + j, book));
            }
        }
    }

}

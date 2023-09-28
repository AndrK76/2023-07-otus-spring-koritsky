package ru.otus.andrk.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;

@ChangeLog(order = "001")
public class InitDatabase {
    @ChangeSet(order = "000", id = "dropDB", author = "AndrK", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "createSequences", author = "AndrK")
    public void initSequences(MongoDatabase db) {
        createSequenceInDb(db, Genre.SEQUENCE_NAME);
        createSequenceInDb(db, Author.SEQUENCE_NAME);
        createSequenceInDb(db, Book.SEQUENCE_NAME);
        createSequenceInDb(db, Comment.SEQUENCE_NAME);
    }

    @ChangeSet(order = "010", id = "initGenres", author = "AndrK")
    public void initGenres(MongoDatabase db) {
        addGenreToDb(db, "unknown");
        addGenreToDb(db, "known");
    }

    @ChangeSet(order = "011", id = "initAuthors", author = "AndrK")
    public void initAuthors(MongoDatabase db) {
        addAuthorToDb(db, "unknown");
        addAuthorToDb(db, "known");
    }

    @ChangeSet(order = "012", id = "initBooks", author = "AndrK")
    public void initBooks(MongoDatabase db) {
        addBookToDb(db, "Book without author and genre", null, null);
        addBookToDb(db, "Book 1", 1L, 1L);
        addBookToDb(db, "Book without genre", 2L, null);
        addBookToDb(db, "Book without author", null, 2L);
    }

    @ChangeSet(order = "020", id = "initComments", author = "AndrK")
    public void initComments(MongoDatabase db) {
        addCommentToDb(db, "Comment 1 for book 1", 1L);
        addCommentToDb(db, "Comment 2 for book 1", 1L);
        addCommentToDb(db, "Comment 1 for book 2", 2L);
        addCommentToDb(db, "Comment 1 for book 3", 3L);
        addCommentToDb(db, "Comment 2 for book 3", 3L);
        addCommentToDb(db, "Comment 3 for book 3", 3L);
    }

    private void createSequenceInDb(MongoDatabase db, String seqName) {
        MongoCollection<Document> sequences = db.getCollection("database_sequences");
        var doc = new Document().append("_id", seqName).append("value", 0L);
        sequences.insertOne(doc);
    }


    private void addGenreToDb(MongoDatabase db, String name) {
        var id = getNextSequenceValue(db, Genre.SEQUENCE_NAME);
        MongoCollection<Document> genres = db.getCollection("genres");
        var doc = new Document()
                .append("name", name)
                .append("_id", id)
                .append("_class", "ru.otus.andrk.model.Genre");
        genres.insertOne(doc);
    }

    private void addAuthorToDb(MongoDatabase db, String name) {
        MongoCollection<Document> authors = db.getCollection("authors");
        var id = getNextSequenceValue(db, Author.SEQUENCE_NAME);
        var doc = new Document()
                .append("name", name)
                .append("_id", id)
                .append("_class", "ru.otus.andrk.model.Author");
        ;
        authors.insertOne(doc);
    }

    private void addBookToDb(MongoDatabase db, String name, Long authorId, Long genreId) {
        MongoCollection<Document> books = db.getCollection("books");
        var id = getNextSequenceValue(db, Book.SEQUENCE_NAME);
        var doc = new Document()
                .append("name", name)
                .append("_id", id)
                .append("_class", "ru.otus.andrk.model.Book");
        if (authorId != null) {
            MongoCollection<Document> authors = db.getCollection("authors");
            var authorFilter = new Document().append("_id", authorId);
            var author = authors.find(authorFilter).first();
            doc.put("author", author);
        }
        if (genreId != null) {
            MongoCollection<Document> genres = db.getCollection("authors");
            var genreFilter = new Document().append("_id", genreId);
            var genre = genres.find(genreFilter).first();
            doc.put("genre", genre);
        }
        books.insertOne(doc);
    }

    private void addCommentToDb(MongoDatabase db, String text, Long bookId) {
        MongoCollection<Document> comments = db.getCollection("comments");
        var id = getNextSequenceValue(db, Comment.SEQUENCE_NAME);

        var bookRef = new Document()
                .append("$ref", "books")
                .append("$id", bookId);
        var doc = new Document()
                .append("text", text)
                .append("_id", id)
                .append("book", bookRef)
                .append("_class", "ru.otus.andrk.model.Comment");
        comments.insertOne(doc);
    }


    private Long getNextSequenceValue(MongoDatabase db, String seqName) {
        MongoCollection<Document> sequences = db.getCollection("database_sequences");
        var doc = new Document().append("_id", seqName);
        var item = sequences.find(doc).first();
        assert item != null;
        long nextVal = (long) item.get("value") + 1;
        item.put("value", nextVal);
        sequences.replaceOne(doc, item);
        return nextVal;
    }
}


package ru.otus.andrk.repository;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Comment;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.util.SequenceGenerator;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@DataMongoTest
@ComponentScan(basePackageClasses = {BookRepositoryCustomImpl.class})
class BookRepositoryCustomTest {

    @Autowired
    MongoTemplate template;

    @Autowired
    BookRepositoryCustomImpl repository;

    @MockBean
    SequenceGenerator generator;

    private static final AtomicLong sequenceValue = new AtomicLong(99L);

    @BeforeEach
    public void initGenerator() {
        when(generator.getValue(Mockito.anyString()))
                .thenAnswer(i -> sequenceValue.incrementAndGet());
    }

    @Test
    public void shouldSetAutoincrementIdToBookWhen_insertBook() {
        synchronized (sequenceValue) {
            var lastId = generator.getValue(Book.SEQUENCE_NAME);
            var actualBook = repository.insertBook(createBook());

            assertThat(actualBook)
                    .isNotNull()
                    .returns(lastId + 1L, Book::getId);
        }
    }

    @Test
    public void shouldInsertOnlyOneDocumentWhen_insertBook() {
        var newBook = repository.insertBook(createBook());
        var actualBooks = getBooksById(newBook.getId());
        assertThat(actualBooks).hasSize(1);
    }

    @Test
    public void shouldCallSequenceGeneratorWhen_insertBook() {
        repository.insertBook(createBook());
        verify(generator, times(1)).getValue(any());
    }

    @ParameterizedTest
    @MethodSource("getNewBooks")
    public void shouldInsertAndReturnCorrectBookWhen_insertBook(Book book) {
        var actualBook = repository.insertBook(book);
        assertThatBooksHaveEqualContent(actualBook, book);

        var storedBook = getBooksById(actualBook.getId()).get(0);
        assertThatBooksHaveEqualContent(storedBook, book);
    }

    @Test
    public void shouldDeleteBookAndCommentsWhen_deleteBook(){
        long bookId = 2L;
        var existBook = getBooksById(bookId).get(0);
        assert existBook != null;
        var existComments = getCommentsForBookWithId(bookId);
        assert existComments.size() > 0;

        repository.deleteBook(bookId);

        var actualBookList = getBooksById(bookId);
        assertThat(actualBookList).hasSize(0);

        var actualComments = getCommentsForBookWithId(bookId);
        assertThat(actualComments).hasSize(0);
    }

    @Test
    public void shouldDontDeleteAuthorAndGenreWhen_deleteBook(){
        long bookId = 5L;
        var existBook = getBooksById(bookId).get(0);
        assert existBook.getAuthor() != null;
        assert existBook.getGenre() != null;
        assert existBook != null;
        var existAuthor = getAuthorById(existBook.getAuthor().getId());
        var existGenre = getGenreById(existBook.getGenre().getId());
        assert existAuthor != null;
        assert existGenre != null;

        repository.deleteBook(bookId);
        var actualAuthor = getAuthorById(existAuthor.getId());
        var actualGenre = getGenreById(existGenre.getId());

        assertThat(actualAuthor).isNotNull();
        assertThat(actualGenre).isNotNull();
    }

    List<Book> getBooksById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Book.class);
    }

    private Author getAuthorById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Author.class).stream().findFirst().orElse(null);
    }

    private Genre getGenreById(long id) {
        var query = Query.query(where("_id").is(id));
        return template.find(query, Genre.class).stream().findFirst().orElse(null);
    }

    List<Comment> getCommentsForBookWithId(long bookId) {
        var bookWhere = new Document().append("$ref", "books").append("$id", bookId);
        var query = Query.query(Criteria.where("book").is(bookWhere));
        return template.find(query, Comment.class);
    }

    private Book createBook() {
        return new Book("Новая книга", getAuthorById(1L), getGenreById(1L));
    }

    private static Stream<Book> getNewBooks() {
        return Stream.of(
                new Book("Книга 1", null, null),
                new Book("Книга 2", new Author(1L, "автор1"), null),
                new Book("Книга 3", null, new Genre(1L, "жанр1")),
                new Book("Книга 4", new Author(1L, "автор1"), new Genre(1L, "жанр1"))
        );
    }

    private void assertThatBooksHaveEqualContent(Book actualBook, Book expectedBook){
        assertThat(actualBook.getName()).isEqualTo(expectedBook.getName());
        if (expectedBook.getAuthor()==null){
            assertThat(actualBook.getAuthor()).isNull();
        } else {
            assertThat(actualBook.getAuthor().getId()).isEqualTo(expectedBook.getAuthor().getId());
            assertThat(actualBook.getAuthor().getName()).isEqualTo(expectedBook.getAuthor().getName());
        }
        if (expectedBook.getGenre()==null){
            assertThat(actualBook.getGenre()).isNull();
        } else {
            assertThat(actualBook.getGenre().getId()).isEqualTo(expectedBook.getGenre().getId());
            assertThat(actualBook.getGenre().getName()).isEqualTo(expectedBook.getGenre().getName());
        }
    }

}
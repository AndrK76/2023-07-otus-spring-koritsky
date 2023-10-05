package ru.otus.andrk.service;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.repository.AuthorRepository;
import ru.otus.andrk.repository.BookRepository;
import ru.otus.andrk.repository.GenreRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@Import({BookServiceImpl.class, DtoMapper.class})
public class BookServiceLifeCycleTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private AuthorRepository authorRepo;

    @Autowired
    private GenreRepository genreRepo;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private DtoMapper mapper;

    private SessionFactory sessionFactory;

    @BeforeEach
    void initSessionFactory() {
        sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
    }

    @Test
    public void shouldReturnExpectedResultAndDontRaiseErrorAndDoOnlyOneQueryToDbWhenGetAllBooks() {
        em.clear();
        AtomicReference<List<BookDto>> actualResult = new AtomicReference<>();
        assertThatNoException().isThrownBy(() -> actualResult.set(bookService.getAllBooks()));
        assertThat(actualResult.get()).hasSize(4);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    public void shouldReturnExpectedResultAndDontRaiseErrorAndDoOnlyOneQueryToDbWhenGetBookById() {
        em.clear();
        AtomicReference<BookDto> actualResult = new AtomicReference<>();
        assertThatNoException().isThrownBy(() -> actualResult.set(bookService.getBookById(1L)));
        assertThat(actualResult.get()).isNotNull();
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    public void shouldReturnExpectedResultAndDontRaiseErrorAndDoOnlyTwoQueryToDbWhenBookWithComments() {
        em.clear();
        AtomicReference<BookWithCommentsDto> actualResult = new AtomicReference<>();
        assertThatNoException().isThrownBy(() -> actualResult.set(bookService.getBookWithCommentsById(1L)));
        assertThat(actualResult.get())
                .isNotNull()
                .extracting(BookWithCommentsDto::getComments)
                .isNotNull()
                .asList()
                .hasSize(2);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    public void shouldCorrectModifyDataAndDontChangeCommentForBookAndCallExpectedStatementsInBdWhenModifyBook() {
        em.clear();
        var book = bookService.getBookById(1L);
        em.clear();
        bookService.modifyBook(1L, "new name", 2L, book.getId());
        em.flush();
        em.clear();
        var actualResult = bookService.getBookWithCommentsById(1l);
        em.clear();
        assertThat(actualResult)
                .isNotNull()
                .returns("new name", BookWithCommentsDto::getName)
                .returns(2L, r -> r.getAuthor().getId())
                .returns(1L, r -> r.getGenre().getId())
                .extracting(BookWithCommentsDto::getComments)
                .isNotNull()
                .asList()
                .hasSize(2);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(6);
    }

}

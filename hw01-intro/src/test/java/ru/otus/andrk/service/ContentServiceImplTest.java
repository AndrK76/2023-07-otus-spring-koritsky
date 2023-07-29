package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.andrk.dao.ContentDao;
import ru.otus.andrk.model.Question;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("Query Content service DAO Implementation")
public class ContentServiceImplTest {

    private ContentDao dao;
    private TestContentDataSource data;

    private ContentService service;

    @BeforeEach
    void initDao() {
        data = new TestContentDataSource();
        dao = Mockito.mock(ContentDao.class);
        Mockito.when(dao.getQuestions()).thenReturn(data.getQuestions());
        service = new ContentServiceImpl(dao);
    }

    @Test
    void shouldBeInitializedWhenCallCountQueriesAndReturnCorrectResult() {
        AtomicInteger actualCount = new AtomicInteger();
        assertThatNoException().isThrownBy(() -> {
            actualCount.set(service.countQueries());
        });
        Mockito.verify(dao, Mockito.times(1)).getQuestions();
        assertThat(actualCount.get()).isEqualTo(2);
    }

    @Test
    void shouldBeInitializedWhenGetNextQueryAndReturnGivenResult() {
        AtomicReference<Question> question = new AtomicReference<>();
        assertThatNoException().isThrownBy(() -> {
            question.set(service.getNextQuestion());
        });
        Mockito.verify(dao, Mockito.times(1)).getQuestions();
        assertThat(question).hasValueMatching(r->r.equals(data.getQuestion1()));
    }

    @Test
    void shouldCallDaoOnlyOneTimes(){
        service.countQueries();
        service.getNextQuestion();
        service.getNextQuestion();
        service.getNextQuestion();
        Mockito.verify(dao, Mockito.times(1)).getQuestions();
    }

    @Test
    void shouldReturnCorrectResultWhenCallingGetNextQuery(){
        assertThat(service.getNextQuestion()).isEqualTo(data.getQuestion1());
        assertThat(service.getNextQuestion()).isEqualTo(data.getQuestion2());
        assertThat(service.getNextQuestion()).isNull();
        service.reset();
        assertThat(service.getNextQuestion()).isEqualTo(data.getQuestion1());
        assertThat(service.getNextQuestion()).isEqualTo(data.getQuestion2());
    }

}

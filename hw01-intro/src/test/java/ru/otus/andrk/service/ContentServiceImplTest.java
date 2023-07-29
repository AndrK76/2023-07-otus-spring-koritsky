package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.andrk.dao.ContentDao;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Query Content service DAO Implementation")
public class ContentServiceImplTest {

    private ContentDao dao;
    private TestContentDataSource data;

    private ContentService service;

    @BeforeEach
    void initDao() {
        data = new TestContentDataSource();
        dao = mock(ContentDao.class);
        when(dao.getQuestions()).thenReturn(data.getQuestions());
        service = new ContentServiceImpl(dao);
    }


    @Test
    void shouldCallDaoOnlyOneTimes(){
        service.getQuestions();
        service.getQuestions();
        Mockito.verify(dao, times(1)).getQuestions();
    }

    @Test
    void shouldReturnAllSourceDataWhenCallingGetQuestions(){
        var questions = service.getQuestions();
        assertThat(questions)
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(List.of(data.getQuestion1(),data.getQuestion2()));
    }

}

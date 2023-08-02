package ru.otus.andrk.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DAO test content in csv resource")
public class QuestionDaoCsvResourceTest {

    private QuestionDao contentDao;

    @BeforeEach
    void initDao(){
        contentDao = new QuestionDaoCsvResource("data.csv");
    }


    @Test
    void shouldCorrectReadDataFromResourceAndReturnNonEmptyList(){
        assertThat(contentDao.getQuestions())
                .isNotNull()
                .hasSize(5);
    }

}

package ru.otus.andrk.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("DAO test content in csv resource")
public class QuestionDaoCsvResourceTest {



    private QuestionDao makeDao(String resourceName){
        return new QuestionDaoCsvResource(resourceName);
    }

    @Test
    void shouldCorrectReadDataFromExistingResourceAndReturnNonEmptyList(){
        var questionDao = makeDao("data.csv");
        assertThat(questionDao.getQuestions())
                .isNotNull()
                .hasSize(5);
    }

    @Test
    void shouldRaiseContentLoadExceptionWhenReadDataFromNonExistingResource(){
        var questionDao = makeDao("none.csv");
        assertThatThrownBy(questionDao::getQuestions).isInstanceOf(ContentLoadException.class);
    }



}

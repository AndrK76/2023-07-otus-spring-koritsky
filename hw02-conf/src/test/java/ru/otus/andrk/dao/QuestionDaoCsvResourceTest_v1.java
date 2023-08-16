package ru.otus.andrk.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.andrk.config.QuestionsDaoCsvConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ComponentScan("ru.otus.andrk")
@ContextConfiguration(classes = {QuestionsDaoCsvConfig.class})
@TestPropertySource("classpath:test.properties")
@DisplayName("DAO test content in csv resource")
public class QuestionDaoCsvResourceTest_v1 {


    @Autowired
    private QuestionsDaoCsvConfig questionsDaoCsvConfig;

    private QuestionDao makeDao(String resourceName) {
        questionsDaoCsvConfig.setResourceName(resourceName);
        return new QuestionDaoCsvResource(questionsDaoCsvConfig);
    }


    @Test
    void shouldCorrectReadDataFromExistingResourceAndReturnNonEmptyList() {
        var questionDao = makeDao("sample.csv");
        assertThat(questionDao.getQuestions())
                .isNotNull()
                .hasSize(5);
    }

    @Test
    void shouldReadQuestionsWithNonEmptyAnswers() {
        var questions = makeDao("sample.csv").getQuestions();
        assertThat(questions)
                .allMatch(r -> r.getAnswers().size() > 0);
    }


    @Test
    void shouldRaiseContentLoadExceptionWhenReadDataFromNonExistingResource() {
        var questionDao = makeDao("none.csv");
        assertThatThrownBy(questionDao::getQuestions).isInstanceOf(ContentLoadException.class);
    }

}

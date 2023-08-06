package ru.otus.andrk.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.andrk.config.CsvConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ComponentScan("ru.otus.andrk")
@ContextConfiguration(classes = QuestionDaoCsvResourceTest.class)
@PropertySource("classpath:test.properties")
@DisplayName("DAO test content in csv resource")
public class QuestionDaoCsvResourceTest {


    @Autowired
    private CsvConfig csvConfig;

    private QuestionDao makeDao(String resourceName) {
        return new QuestionDaoCsvResource(csvConfig, resourceName);
    }


    @Test
    void shouldCorrectReadDataFromExistingResourceAndReturnNonEmptyList() {
        var questionDao = makeDao("sample.csv");
        assertThat(questionDao.getQuestions())
                .isNotNull()
                .hasSize(5);
    }

    @Test
    void shouldReadQuestionsWithNonEmptyAnswersAndMinAnswerIndexMustBeOne() {
        var questions = makeDao("sample.csv").getQuestions();
        assertThat(questions)
                .allMatch(r -> r.getAnswers().size() > 0)
                .allMatch(r -> r.getAnswers().keySet().stream().filter(k -> k < 1).toList().isEmpty());
    }


    @Test
    void shouldRaiseContentLoadExceptionWhenReadDataFromNonExistingResource() {
        var questionDao = makeDao("none.csv");
        assertThatThrownBy(questionDao::getQuestions).isInstanceOf(ContentLoadException.class);
    }

}

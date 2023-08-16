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
@ContextConfiguration(classes = {QuestionDaoCsvResource.class, QuestionsDaoCsvConfig.class})
@TestPropertySource("classpath:test.properties")
@DisplayName("DAO test content in csv resource")
public class QuestionDaoCsvResourceTest_v2 {

    @Autowired
    private QuestionsDaoCsvConfig questionsDaoCsvConfig;

    @Autowired
    private QuestionDao questionDao;


    @Test
    void shouldCorrectReadDataFromExistingResourceAndReturnNonEmptyList() {
        questionsDaoCsvConfig.setResourceName("sample.csv");
        assertThat(questionDao.getQuestions())
                .isNotNull()
                .hasSize(5);
    }

    @Test
    void shouldReadQuestionsWithNonEmptyAnswers() {
        questionsDaoCsvConfig.setResourceName("sample.csv");
        var questions = questionDao.getQuestions();
        assertThat(questions)
                .allMatch(r -> r.getAnswers().size() > 0);
    }


    @Test
    void shouldRaiseContentLoadExceptionWhenReadDataFromNonExistingResource() {
        questionsDaoCsvConfig.setResourceName("none.csv");
        assertThatThrownBy(questionDao::getQuestions).isInstanceOf(ContentLoadException.class);
    }

}

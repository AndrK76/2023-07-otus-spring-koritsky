package ru.otus.andrk.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import ru.otus.andrk.config.QuestionsDaoCsvConfig;
import ru.otus.andrk.service.i18n.ResourceService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {QuestionDaoCsvResource.class})
@TestPropertySource("classpath:test.properties")
@DisplayName("DAO test content in csv resource")
public class QuestionDaoCsvResourceTest {

    @MockBean
    private QuestionsDaoCsvConfig questionsDaoCsvConfig;

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private ResourceService resourceService;

    @Value("${test-system.questions.resource-folder}")
    private String resourceFolder;

    @Value("${test-system.questions.csv-delimiter}")
    private String csvDelimiter;

    @BeforeEach
    void initResourceService() {
        when(resourceService.getLocalizedResourceName(anyString(), anyString()))
                .thenAnswer(i -> i.getArgument(0) + "/" + i.getArgument(1));
    }

    @BeforeEach
    void initCsvConfig() {
        when(questionsDaoCsvConfig.getCsvDelimiter()).thenReturn(csvDelimiter);
        when(questionsDaoCsvConfig.getResourceFolder()).thenReturn(resourceFolder);
    }

    private void setResourceName(String resourceName) {
        when(questionsDaoCsvConfig.getResourceName()).thenReturn(resourceName);
    }

    @Test
    void shouldCorrectReadDataFromExistingResourceAndReturnNonEmptyList() {
        setResourceName("sample.csv");
        assertThat(questionDao.getQuestions())
                .isNotNull()
                .hasSize(5);
    }

    @Test
    void shouldReadQuestionsWithNonEmptyAnswersAndMinAnswerIndexMustBeOne() {
        setResourceName("sample.csv");
        var questions = questionDao.getQuestions();
        assertThat(questions)
                .allMatch(r -> r.getAnswers().size() > 0);
    }

    @Test
    void shouldRaiseContentLoadExceptionWhenReadDataFromNonExistingResource() {
        setResourceName("none.csv");
        assertThatThrownBy(questionDao::getQuestions).isInstanceOf(ContentLoadException.class);
    }

}

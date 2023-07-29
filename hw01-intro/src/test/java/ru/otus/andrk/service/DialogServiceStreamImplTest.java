package ru.otus.andrk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Service for dialog with user")
public class DialogServiceStreamImplTest {
    private TestContentDataSource data;
    private DialogService dialogService;
    private ByteArrayOutputStream out;


    @BeforeEach
    void initData() {
        data = new TestContentDataSource();
        out = new ByteArrayOutputStream();
        dialogService = new DialogServiceStreamImpl(out);
    }

    @Test
    void shouldContainsQueryTextInOutputWhenCallingShowQuestionWithAnswers() {
        dialogService.showQuestionWithAnswers(data.getQuestion1());
        var strResult = out.toString();
        assertThat(strResult)
                .isNotNull()
                .contains(List.of(
                        data.getQuestion1().getQueryText(),
                        data.getQuestion1().getAnswers().get(0).getAnswerText(),
                        data.getQuestion1().getAnswers().get(1).getAnswerText()
                ));
    }

}

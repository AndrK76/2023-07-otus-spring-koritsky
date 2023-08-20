package ru.otus.andrk.service.dialog;

import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.question.IncorrectAnswerException;

public interface DialogService {
    void showQuestion(Question question);

    String getAnswerText();

    void showRetryTextOnError(IncorrectAnswerException cause);

    void showTestResult(TestResult testResult);

}

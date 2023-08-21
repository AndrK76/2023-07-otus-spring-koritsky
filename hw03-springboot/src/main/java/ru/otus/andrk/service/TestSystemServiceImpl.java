package ru.otus.andrk.service;

import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.auth.StudentLoginService;
import ru.otus.andrk.service.question.AnswerValidatorService;
import ru.otus.andrk.service.question.IncorrectAnswerException;
import ru.otus.andrk.service.dialog.DialogService;
import ru.otus.andrk.service.question.QuestionSourceService;

import java.util.List;

@Service
public class TestSystemServiceImpl implements TestSystemService {

    private final QuestionSourceService questionSourceService;

    private final DialogService dialogService;

    private final AnswerValidatorService answerValidatorService;

    private final StudentLoginService studentLoginService;


    public TestSystemServiceImpl(
            QuestionSourceService questionSourceService,
            DialogService dialogService,
            AnswerValidatorService answerValidatorService,
            StudentLoginService studentLoginService) {
        this.questionSourceService = questionSourceService;
        this.dialogService = dialogService;
        this.answerValidatorService = answerValidatorService;
        this.studentLoginService = studentLoginService;
    }

    @Override
    public void runTest() {
        var student = studentLoginService.getStudent();
        var testResult = new TestResult(student);
        var questions = questionSourceService.getQuestions();

        for (var question : questions) {
            dialogService.showQuestion(question);
            var answers = getStudentAnswer(question);
            testResult.addResult(answerValidatorService.checkAnswer(question, answers));
        }

        dialogService.showTestResult(testResult);
    }

    private List<Answer> getStudentAnswer(Question question) {
        try {
            var answerText = dialogService.getAnswerText();
            return answerValidatorService.getAnswersFromString(question, answerText);
        } catch (IncorrectAnswerException e) {
            dialogService.showRetryTextOnError(e);
            return getStudentAnswer(question);
        }
    }
}

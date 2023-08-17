package ru.otus.andrk.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.InformationMessage;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.dialog.DialogService;
import ru.otus.andrk.service.question.AbstractIncorrectAnswerException;
import ru.otus.andrk.service.question.AnswerValidatorService;
import ru.otus.andrk.service.question.QuestionSourceService;
import ru.otus.andrk.service.student.StudentInfoService;

import java.util.List;

@Service
public class TestSystemServiceImpl implements TestSystemService {

    private final QuestionSourceService questionSourceService;

    private final AnswerValidatorService answerValidatorService;

    private final DialogService dialogService;

    private final StudentInfoService studentInfoService;

    private final ConversionService conversionService;

    public TestSystemServiceImpl(
            QuestionSourceService questionSourceService,
            AnswerValidatorService answerValidatorService,
            @Lazy DialogService dialogService,
            StudentInfoService studentInfoService,
            ConversionService conversionService) {
        this.questionSourceService = questionSourceService;
        this.answerValidatorService = answerValidatorService;
        this.dialogService = dialogService;
        this.studentInfoService = studentInfoService;
        this.conversionService = conversionService;
    }

    @Override
    public void runTest() {
        Student student = studentInfoService.getStudent();
        TestResult testResult = new TestResult(student);

        var questions = questionSourceService.getQuestions();
        for (var question : questions) {
            dialogService.displayText(conversionService.convert(question, String.class));
            var studentAnswer = getStudentAnswer(question);
            testResult.addResult(answerValidatorService.checkAnswer(question, studentAnswer));
        }
        dialogService.displayText(conversionService.convert(testResult, String.class));
    }

    private List<Answer> getStudentAnswer(Question question) {
        try {
            return answerValidatorService.getAnswersFromString(question, dialogService.readText());
        } catch (AbstractIncorrectAnswerException e) {
            dialogService.displayText(conversionService.convert(e, String.class)
                    + ", " + conversionService.convert(InformationMessage.RETRY_TEXT, String.class));
            return getStudentAnswer(question);
        }
    }


}

package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.dialogs.DialogService;
import ru.otus.andrk.service.questions.IncorrectAnswerException;
import ru.otus.andrk.service.questions.AnswerValidatorService;
import ru.otus.andrk.service.questions.QuestionSourceService;
import ru.otus.andrk.service.students.StudentInfoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestSystemServiceImpl implements TestSystemService {

    private final QuestionSourceService questionSourceService;

    private final AnswerValidatorService answerValidatorService;

    private final DialogService dialogService;

    private final StudentInfoService studentInfoService;

    private final ConversionService conversionService;

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
        } catch (IncorrectAnswerException e) {
            dialogService.displayText(e.getMessage() + ", please retry");
            return getStudentAnswer(question);
        }
    }


}

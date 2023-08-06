package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.TestSystemConfig;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.dialogs.DialogService;
import ru.otus.andrk.service.questions.AnswerValidator;
import ru.otus.andrk.service.questions.IncorrectAnswerException;
import ru.otus.andrk.service.questions.QuestionConverterService;
import ru.otus.andrk.service.questions.QuestionService;
import ru.otus.andrk.service.students.StudentInfoService;
import ru.otus.andrk.service.testresults.TestResultConverterService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestSystemServiceImpl implements TestSystemService {

    private final TestSystemConfig testSystemConfig;

    private final QuestionService questionService;

    private final QuestionConverterService questionConverterService;

    private final DialogService dialogService;

    private final StudentInfoService studentInfoService;

    private final AnswerValidator answerValidator;

    private final TestResultConverterService testResultConverterService;

    @Override
    public void runTest() {
        Student student = studentInfoService.getUserName();
        TestResult testResult = new TestResult(student);

        var questions = questionService.getNoMoreQuestions(testSystemConfig.getMaxQueries());
        for (var question : questions) {
            dialogService.displayText(questionConverterService.queryToString(question));
            var studentAnswer = getStudentAnswer(question);
            testResult.addResult(answerValidator.checkAnswer(question, studentAnswer));
        }
        dialogService.displayText(testResultConverterService.resultToString(testResult));
    }

    private List<Answer> getStudentAnswer(Question question) {
        try {
            return questionConverterService.answersFromString(question, dialogService.readText());
        } catch (IncorrectAnswerException e) {
            dialogService.displayText(e.getMessage() + ", please retry");
            return getStudentAnswer(question);
        }
    }


}

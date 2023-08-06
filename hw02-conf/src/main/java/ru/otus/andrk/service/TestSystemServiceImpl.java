package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.config.TestSystemConfig;
import ru.otus.andrk.dao.QuestionDao;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.dialogs.DialogService;
import ru.otus.andrk.service.questions.AnswerValidator;
import ru.otus.andrk.service.questions.IncorrectAnswerException;
import ru.otus.andrk.service.questions.QueryConverterService;
import ru.otus.andrk.service.students.StudentInfoService;
import ru.otus.andrk.service.testresults.TestResultConverterService;

@Service
@RequiredArgsConstructor
public class TestSystemServiceImpl implements TestSystemService {

    private final TestSystemConfig testSystemConfig;

    private final QuestionDao questionDao;

    private final QueryConverterService queryConverterService;

    private final DialogService dialogService;

    private final StudentInfoService studentInfoService;

    private final AnswerValidator answerValidator;

    private final TestResultConverterService testResultConverterService;

    @Override
    public void runTest() {
        Student student = studentInfoService.getUserName();
        TestResult testResult = new TestResult(student);

        for (var question : questionDao.getQuestions().stream()
                .limit(testSystemConfig.getMaxQueries()).toList()) {
            dialogService.displayText(queryConverterService.queryToString(question));
            boolean haveCorrectAnswer = false;
            while (!haveCorrectAnswer) {
                try {
                    var answers = queryConverterService.answersFromString(question, dialogService.readText());
                    haveCorrectAnswer = true;
                    testResult.addResult(answerValidator.checkAnswer(question, answers));
                } catch (IncorrectAnswerException e) {
                    dialogService.displayText(e.getMessage() + ", please retry");
                }
            }
        }
        dialogService.displayText(testResultConverterService.resultToString(testResult));
    }


}

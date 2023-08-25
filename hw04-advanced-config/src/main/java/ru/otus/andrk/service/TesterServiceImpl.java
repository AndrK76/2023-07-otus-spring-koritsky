package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Answer;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.Student;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.dialog.Message;
import ru.otus.andrk.service.dialog.MessageService;
import ru.otus.andrk.service.question.AnswerValidatorService;
import ru.otus.andrk.service.question.IncorrectAnswerException;
import ru.otus.andrk.service.question.QuestionLoadException;
import ru.otus.andrk.service.question.QuestionSourceService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TesterServiceImpl implements TesterService {

    private final QuestionSourceService questionSourceService;

    private final AnswerValidatorService answerValidatorService;

    private final MessageService messageService;

    private final ConversionService conversionService;

    @Override
    public String runTestForStudent(Student student) {
        try {
            var questions = questionSourceService.getQuestions();
            var testResult = new TestResult(student);
            for (var question : questions) {
                messageService.showMessage(
                        conversionService.convert(question, Message.class));
                messageService.showMessage(
                        switch (question.getQueryType()) {
                            case ONE_VALID_ANSWER -> new Message("ONE_VALID_ANSWER");
                            case MANY_VALID_ANSWERS -> new Message("MANY_VALID_ANSWERS");
                        });
                var answers = getStudentAnswer(question);
                testResult.addResult(answerValidatorService.checkAnswer(question, answers));
            }
            return messageService.getMessageAsText(
                    conversionService.convert(testResult, Message.class));
        } catch (QuestionLoadException e) {
            return messageService.getMessageAsText(new Message("FAIL_LOAD_QUESTIONS"));
        }
    }

    private List<Answer> getStudentAnswer(Question question) {
        try {
            var answerText = messageService.readText();
            return answerValidatorService.getAnswersFromString(question, answerText);
        } catch (IncorrectAnswerException e) {
            String errMessageKey = switch (e.getClass().getSimpleName()) {
                case "IncorrectAnswerCharactersException" -> "INVALID_ANSWER_CHARACTERS";
                case "IncorrectAnswerNumberException" -> "INVALID_ANSWER_NUMBER";
                default -> "INVALID_ANSWER_OTHER";
            };
            messageService.showMessage(
                    Message.builder().addMessage(errMessageKey)
                            .addText(", ")
                            .addMessage("RETRY_TEXT")
                            .build());
            return getStudentAnswer(question);
        }
    }
}

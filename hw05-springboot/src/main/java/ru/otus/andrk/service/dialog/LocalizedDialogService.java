package ru.otus.andrk.service.dialog;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.model.TestResult;
import ru.otus.andrk.service.i18n.MessageProvider;
import ru.otus.andrk.service.io.IOService;
import ru.otus.andrk.service.question.IncorrectAnswerException;

@Service
public class LocalizedDialogService implements DialogService {

    private final IOService ioService;

    private final MessageProvider messageProvider;

    private final ConversionService conversionService;


    public LocalizedDialogService(
            IOService ioService,
            MessageProvider messageProvider,
            ConversionService conversionService) {
        this.ioService = ioService;
        this.messageProvider = messageProvider;
        this.conversionService = conversionService;
    }

    @Override
    public void showQuestion(Question question) {
        String answerVariantText = switch (question.getQueryType()) {
            case ONE_VALID_ANSWER -> messageProvider.getMessage("ONE_VALID_ANSWER");
            case MANY_VALID_ANSWERS -> messageProvider.getMessage("MANY_VALID_ANSWERS");
        };
        String textForShow = conversionService.convert(question, String.class) + answerVariantText;
        ioService.displayText(textForShow);
    }

    @Override
    public String getAnswerText() {
        return ioService.readText();
    }

    @Override
    public void showRetryTextOnError(IncorrectAnswerException cause) {
        String errMessageKey = switch (cause.getClass().getSimpleName()) {
            case "IncorrectAnswerCharactersException" -> "INVALID_ANSWER_CHARACTERS";
            case "IncorrectAnswerNumberException" -> "INVALID_ANSWER_NUMBER";
            default -> "INVALID_ANSWER_OTHER";
        };
        String message = messageProvider.getMessage(errMessageKey) + ", " + messageProvider.getMessage("RETRY_TEXT");
        ioService.displayText(message);
    }

    @Override
    public void showTestResult(TestResult testResult) {
        ioService.displayText(conversionService.convert(testResult, String.class));
    }
}

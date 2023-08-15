package ru.otus.andrk.converter;


import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class QuestionToStringConverter implements Converter<Question, String> {

    private final MessageService messageService;

    public QuestionToStringConverter(@Lazy MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public String convert(Question question) {
        StringBuilder sb = new StringBuilder(question.getQueryText())
                .append("\n")
                .append(messageService.getMessage("ANSWERS"))
                .append("\n");
        AtomicInteger currIndex = new AtomicInteger(0);
        question.getAnswers()
                .forEach((answer) -> sb
                        .append("\t")
                        .append(currIndex.incrementAndGet())
                        .append(". ")
                        .append(answer.getAnswerText())
                        .append("\n"));
        sb.append(
                switch (question.getQueryType()) {
                    case ONE_VALID_ANSWER -> messageService.getMessage("ONE_VALID_ANSWER");
                    case MANY_VALID_ANSWERS -> messageService.getMessage("MANY_VALID_ANSWERS");
                }
        );
        return sb.toString();
    }


}

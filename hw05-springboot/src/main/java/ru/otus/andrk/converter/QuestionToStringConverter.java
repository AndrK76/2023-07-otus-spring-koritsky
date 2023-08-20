package ru.otus.andrk.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.service.i18n.MessageProvider;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class QuestionToStringConverter implements Converter<Question, String> {

    private final MessageProvider messageProvider;

    public QuestionToStringConverter(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }


    @Override
    public String convert(Question question) {
        StringBuilder sb = new StringBuilder(question.getQueryText())
                .append("\n")
                .append(messageProvider.getMessage("ANSWERS"))
                .append("\n");
        AtomicInteger currIndex = new AtomicInteger(0);
        question.getAnswers()
                .forEach((answer) -> sb
                        .append("\t")
                        .append(currIndex.incrementAndGet())
                        .append(". ")
                        .append(answer.getAnswerText())
                        .append("\n"));
        return sb.toString();
    }


}

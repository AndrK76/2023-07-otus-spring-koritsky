package ru.otus.andrk.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Question;
import ru.otus.andrk.service.dialog.Message;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class QuestionToMessageConverter implements Converter<Question, Message> {


    @Override
    public Message convert(Question question) {
        var messageBuilder = Message.builder();
        messageBuilder.addText(question.getQueryText())
                .addText("\n")
                .addMessage("ANSWERS")
                .addText("\n");
        AtomicInteger currIndex = new AtomicInteger(0);
        question.getAnswers()
                .forEach((answer) -> messageBuilder
                        .addText("\t")
                        .addText(String.valueOf(currIndex.incrementAndGet()))
                        .addText(". ")
                        .addText(answer.getAnswerText())
                        .addText("\n"));
        return messageBuilder.build();
    }


}

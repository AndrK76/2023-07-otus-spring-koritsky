package ru.otus.andrk.converters;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.model.Question;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class QuestionToStringConverter implements Converter<Question, String> {

    @Override
    public String convert(Question question) {
        StringBuilder sb = new StringBuilder(question.getQueryText())
                .append("\n")
                .append("Answers:\n");
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
                    case ONE_VALID_ANSWER -> "Enter valid number:";
                    case MANY_VALID_ANSWERS -> "Enter all valid numbers via comma:";
                }
        );
        return sb.toString();
    }

}

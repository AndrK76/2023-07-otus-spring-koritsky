package ru.otus.andrk.service;

import ru.otus.andrk.model.Question;

import java.util.concurrent.atomic.AtomicInteger;

public class ConverterServiceSimple implements ConverterService {
    @Override
    public String queryToString(Question question) {
        AtomicInteger answerIndex = new AtomicInteger();
        StringBuilder sb = new StringBuilder(question.getQueryText())
                .append("\n")
                .append("Answers:\n");
        question.getAnswers()
                .forEach(answer -> sb
                        .append("\t")
                        .append(answerIndex.incrementAndGet())
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

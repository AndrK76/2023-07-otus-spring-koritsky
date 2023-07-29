package ru.otus.andrk.service;

import ru.otus.andrk.model.Question;

import java.io.OutputStream;
import java.io.PrintStream;

public class DialogServiceStreamImpl implements DialogService {

    private final PrintStream outStream;


    public DialogServiceStreamImpl() {
        this.outStream = System.out;
    }

    public DialogServiceStreamImpl(OutputStream outStream) {
        this.outStream = new PrintStream(outStream);
    }

    @Override
    public void showQuestionWithAnswers(Question question) {
        StringBuilder sb = new StringBuilder("Query #" + question.getNum() + ".\n")
                .append(question.getQueryText())
                .append("\n")
                .append("Answers:\n");
        question.getAnswers()
                .forEach(answer -> sb
                        .append("\t")
                        .append(answer.getNum())
                        .append(". ")
                        .append(answer.getAnswerText())
                        .append("\n"));
        sb.append(
                        switch (question.getQueryType()) {
                            case ONE_VALID_ANSWER -> "Enter valid number:";
                            case MANY_VALID_ANSWERS -> "Enter all valid numbers via comma:";
                        }
                )
                .append("\n");
        outStream.println(sb.toString());
    }
}

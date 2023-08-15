package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Answer {
    private String answerText;

    private boolean valid;
}

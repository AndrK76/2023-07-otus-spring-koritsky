package ru.otus.andrk.model;

import lombok.Getter;

import java.util.Arrays;

public enum QuestionType {
    ONE_VALID_ANSWER("One"),
    MANY_VALID_ANSWERS("Many");

    @Getter
    private final String displayName;

    QuestionType(String displayName) {
        this.displayName = displayName;
    }

    public static QuestionType byName(String name) {
        return Arrays.stream(QuestionType.values())
                .filter(r -> r.getDisplayName().equalsIgnoreCase(name))
                .findFirst().orElseThrow();
    }

}

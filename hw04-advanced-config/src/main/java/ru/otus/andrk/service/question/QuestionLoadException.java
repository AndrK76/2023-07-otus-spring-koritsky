package ru.otus.andrk.service.question;

public class QuestionLoadException extends RuntimeException {
    public QuestionLoadException(Throwable cause) {
        super(cause);
    }

    public QuestionLoadException(String message) {
        super(message);
    }

    public QuestionLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}

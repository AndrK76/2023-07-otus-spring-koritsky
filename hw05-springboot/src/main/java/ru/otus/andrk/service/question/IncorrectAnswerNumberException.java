package ru.otus.andrk.service.question;

public class IncorrectAnswerNumberException extends IncorrectAnswerException {
    public IncorrectAnswerNumberException(String message) {
        super(message);
    }

    public IncorrectAnswerNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}

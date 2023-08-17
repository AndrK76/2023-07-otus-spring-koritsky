package ru.otus.andrk.service.question;

public class IncorrectAnswerNumberException extends AbstractIncorrectAnswerException {
    public IncorrectAnswerNumberException(String message) {
        super(message);
    }

    public IncorrectAnswerNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}

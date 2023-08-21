package ru.otus.andrk.service.question;

public class IncorrectAnswerCharactersException extends IncorrectAnswerException {
    public IncorrectAnswerCharactersException(String message) {
        super(message);
    }

    public IncorrectAnswerCharactersException(String message, Throwable cause) {
        super(message, cause);
    }

}

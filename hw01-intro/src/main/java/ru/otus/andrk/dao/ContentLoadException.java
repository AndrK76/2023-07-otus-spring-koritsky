package ru.otus.andrk.dao;

public class ContentLoadException extends RuntimeException{
    public ContentLoadException(Throwable cause) {
        super(cause);
    }

    public ContentLoadException(String message) {
        super(message);
    }

    public ContentLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}

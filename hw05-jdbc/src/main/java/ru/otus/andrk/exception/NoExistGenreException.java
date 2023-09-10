package ru.otus.andrk.exception;

public class NoExistGenreException extends KnownLibraryManipulationException {
    public NoExistGenreException() {
        super();
    }

    public NoExistGenreException(Throwable cause) {
        super(cause);
    }
}

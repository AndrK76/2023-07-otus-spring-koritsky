package ru.otus.andrk.excepton;

public class NoExistGenreException extends KnownLibraryManipulationException {
    public NoExistGenreException() {
        super();
    }

    public NoExistGenreException(Throwable cause) {
        super(cause);
    }
}

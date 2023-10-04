package ru.otus.andrk.excepton;

public class KnownLibraryManipulationException extends RuntimeException {
    public KnownLibraryManipulationException() {
        super();
    }

    public KnownLibraryManipulationException(Throwable cause) {
        super(cause);
    }
}

package org.personalblogapi.exception;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("Data already exists");
    }

    public ConflictException(String message) {
        super(message);
    }
}

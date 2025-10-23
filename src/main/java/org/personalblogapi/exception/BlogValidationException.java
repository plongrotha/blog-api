package org.personalblogapi.exception;

public class BlogValidationException extends RuntimeException {
    public BlogValidationException(String message) {
        super(message);
    }
}
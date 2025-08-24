package com.careconnect.exception;

public class DuplicateResource extends RuntimeException {
    private final String field;

    public DuplicateResource(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

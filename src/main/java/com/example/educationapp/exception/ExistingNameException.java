package com.example.educationapp.exception;

public class ExistingNameException extends RuntimeException {
    public ExistingNameException(String message) {
        super(message);
    }
}

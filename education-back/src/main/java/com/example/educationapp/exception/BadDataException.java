package com.example.educationapp.exception;

public class BadDataException extends RuntimeException{
    public BadDataException(String message) {
        super(message);
    }
}

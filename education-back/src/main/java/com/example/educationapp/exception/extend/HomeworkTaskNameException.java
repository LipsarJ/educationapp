package com.example.educationapp.exception.extend;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.exception.BadDataException;

public class HomeworkTaskNameException extends BadDataException {
    public HomeworkTaskNameException(String message, Errors errorCode) {
        super(message, errorCode);
    }
}

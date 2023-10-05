package com.example.educationapp.exception.extend;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.exception.BadDataException;

public class LessonNameException extends BadDataException {
    public LessonNameException(String message, Errors errorCode) {
        super(message, errorCode);
    }
}

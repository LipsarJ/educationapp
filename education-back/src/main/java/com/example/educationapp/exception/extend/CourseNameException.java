package com.example.educationapp.exception.extend;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.exception.BadDataException;

public class CourseNameException extends BadDataException {
    public CourseNameException(String message, Errors errorCode) {
        super(message, errorCode);
    }
}

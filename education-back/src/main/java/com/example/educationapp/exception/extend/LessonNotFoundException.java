package com.example.educationapp.exception.extend;

import com.example.educationapp.exception.NotFoundException;

public class LessonNotFoundException extends NotFoundException {
    public LessonNotFoundException(String message) {
        super(message);
    }
}

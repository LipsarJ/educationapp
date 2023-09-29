package com.example.educationapp.exception.extend;

import com.example.educationapp.exception.NotFoundException;

public class CourseNotFoundException extends NotFoundException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}

package com.example.educationapp.exception.extend;

import com.example.educationapp.exception.NotFoundException;

public class HomeworkTaskNotFoundException extends NotFoundException {
    public HomeworkTaskNotFoundException(String message) {
        super(message);
    }
}

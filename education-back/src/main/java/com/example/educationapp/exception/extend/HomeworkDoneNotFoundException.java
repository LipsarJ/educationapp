package com.example.educationapp.exception.extend;

import com.example.educationapp.exception.NotFoundException;

public class HomeworkDoneNotFoundException extends NotFoundException {
    public HomeworkDoneNotFoundException(String message) {
        super(message);
    }
}

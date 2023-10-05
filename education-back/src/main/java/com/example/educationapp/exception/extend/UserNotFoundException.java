package com.example.educationapp.exception.extend;

import com.example.educationapp.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
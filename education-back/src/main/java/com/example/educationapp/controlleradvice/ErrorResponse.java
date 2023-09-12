package com.example.educationapp.controlleradvice;

public record ErrorResponse(String message, LoginErrors errorCode) {
}

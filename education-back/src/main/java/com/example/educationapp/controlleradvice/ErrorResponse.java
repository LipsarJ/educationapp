package com.example.educationapp.controlleradvice;

public record ErrorResponse(String message, AuthErrors errorCode) {
}

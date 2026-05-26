package com.example.projecthub.exception;

public class AccessDeniedAppException extends RuntimeException {
    public AccessDeniedAppException(String message) {
        super(message);
    }
}

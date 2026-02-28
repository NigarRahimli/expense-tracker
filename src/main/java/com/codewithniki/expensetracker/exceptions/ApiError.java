package com.codewithniki.expensetracker.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiError {

    private int statusCode;
    private String message;
    private Map<String, String> validationErrors;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(int status, String message) {
        this.statusCode = status;
        this.message = message;
    }

    public ApiError(int status, String message, Map<String, String> validationErrors) {
        this.statusCode = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}

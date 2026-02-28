package com.codewithniki.expensetracker.model.dtos.auth;

import java.time.LocalDateTime;

public class RegisterResponse {

    private String message;
    private LocalDateTime timestamp;
    private String nextStepUrl;

    public RegisterResponse(String message, String nextStepUrl) {
        this.message = message;
        this.nextStepUrl = nextStepUrl;
        this.timestamp = LocalDateTime.now();
    }

    /* getters */

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getNextStepUrl() {
        return nextStepUrl;
    }
}
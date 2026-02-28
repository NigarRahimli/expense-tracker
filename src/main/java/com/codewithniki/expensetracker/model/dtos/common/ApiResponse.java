package com.codewithniki.expensetracker.model.dtos.common;

import java.time.LocalDateTime;

public class ApiResponse {

    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String next;

    private ApiResponse(String message, int status, String next) {
        this.message = message;
        this.status = status;
        this.next = next;
        this.timestamp = LocalDateTime.now();
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(message, 200, null);
    }

    public static ApiResponse success(String message, String next) {
        return new ApiResponse(message, 200, next);
    }

    // getters
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getNext() {
        return next;
    }
}

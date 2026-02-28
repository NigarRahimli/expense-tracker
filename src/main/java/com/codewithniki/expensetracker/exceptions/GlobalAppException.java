package com.codewithniki.expensetracker.exceptions;

public class GlobalAppException extends RuntimeException {

    public GlobalAppException(String message) {
        super(message);
    }

    public GlobalAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
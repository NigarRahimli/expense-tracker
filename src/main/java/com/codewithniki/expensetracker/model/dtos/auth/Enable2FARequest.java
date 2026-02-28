package com.codewithniki.expensetracker.model.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Enable2FARequest {

    @NotBlank
    @Pattern(
            regexp = "^\\+[1-9]\\d{7,14}$",
            message = "Phone number must be in international format (e.g. +994501234567)"
    )
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

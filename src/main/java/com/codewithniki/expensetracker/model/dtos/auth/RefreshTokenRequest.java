package com.codewithniki.expensetracker.model.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }
}
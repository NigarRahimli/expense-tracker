package com.codewithniki.expensetracker.model.dtos.auth;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private boolean otpRequired;
    private String twoFactorToken;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String accessToken,
                        String refreshToken,
                        boolean otpRequired,
                        String twoFactorToken,
                        String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.otpRequired = otpRequired;
        this.twoFactorToken = twoFactorToken;
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isOtpRequired() {
        return otpRequired;
    }

    public void setOtpRequired(boolean otpRequired) {
        this.otpRequired = otpRequired;
    }

    public String getTwoFactorToken() {
        return twoFactorToken;
    }

    public void setTwoFactorToken(String twoFactorToken) {
        this.twoFactorToken = twoFactorToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

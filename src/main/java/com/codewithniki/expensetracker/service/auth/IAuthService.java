package com.codewithniki.expensetracker.service.auth;

import com.codewithniki.expensetracker.model.dtos.auth.*;

public interface IAuthService {

    RegisterResponse register(RegisterRequest request);

    void verifyEmail(String code);

    void resendEmailVerification(String email);

    AuthResponse login(LoginRequest request);

    AuthResponse verifyTwoFactorLogin(String twoFactorToken, String code);
}
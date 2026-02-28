package com.codewithniki.expensetracker.controller;

import com.codewithniki.expensetracker.model.dtos.auth.*;
import com.codewithniki.expensetracker.model.dtos.common.ApiResponse;
import com.codewithniki.expensetracker.model.dtos.user.UserResponse;
import com.codewithniki.expensetracker.service.auth.IAuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/verify-email")
    public ApiResponse verifyEmail(
            @RequestBody @Valid VerifyEmailRequest request
    ) {
        authService.verifyEmail(request.getCode());
        return ApiResponse.success(
                "Email verified successfully",
                "/auth/login"
        );
    }
    @PostMapping("/resend-verification")
    public ApiResponse resendVerification(
            @RequestBody @Valid ResendVerificationRequest request) {

        authService.resendEmailVerification(request.getEmail());

        return ApiResponse.success(
                "Verification code resent successfully. Please check your email."
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }


    @PostMapping("/verify-2fa")
    public AuthResponse verify2fa(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Verify2FaRequest request
    ) {
        String token = authHeader.replace("Bearer ", "");

        return authService.verifyTwoFactorLogin(
                token,
                request.getCode()
        );
    }

}

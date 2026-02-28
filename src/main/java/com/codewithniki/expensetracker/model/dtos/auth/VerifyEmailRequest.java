package com.codewithniki.expensetracker.model.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VerifyEmailRequest {

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

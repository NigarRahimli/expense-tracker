package com.codewithniki.expensetracker.model.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Verify2FaRequest {

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;

    public String getCode() {
        return code;
    }
}
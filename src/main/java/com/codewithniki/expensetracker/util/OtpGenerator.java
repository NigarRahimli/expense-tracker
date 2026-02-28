package com.codewithniki.expensetracker.util;

import java.security.SecureRandom;

public final class OtpGenerator {

    private static final SecureRandom random = new SecureRandom();

    private OtpGenerator() {}

    public static String generate6Digits() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
package com.codewithniki.expensetracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.signing-key}")
    private String signingKey;

    @Value("${jwt.access-token-minutes}")
    private long accessTokenMinutes;

    @Value("${jwt.refresh-token-days}")
    private long refreshTokenDays;

    @Value("${jwt.two-factor-minutes}")
    private long twoFactorMinutes;

    public String getIssuer() { return issuer; }
    public String getAudience() { return audience; }
    public String getSigningKey() { return signingKey; }
    public long getAccessTokenMinutes() { return accessTokenMinutes; }
    public long getRefreshTokenDays() { return refreshTokenDays; }
    public long getTwoFactorMinutes() { return twoFactorMinutes; }
}
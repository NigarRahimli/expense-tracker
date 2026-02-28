package com.codewithniki.expensetracker.service.jwt;

import com.codewithniki.expensetracker.config.JwtProperties;
import com.codewithniki.expensetracker.model.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties properties;
    private final SecretKey key;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSigningKey().getBytes());
    }

    // ================= TOKEN GENERATION =================

    public String generateAccessToken(User user) {
        return buildToken(user, "ACCESS",
                properties.getAccessTokenMinutes(), ChronoUnit.MINUTES);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, "REFRESH",
                properties.getRefreshTokenDays(), ChronoUnit.DAYS);
    }

    public String generateTwoFactorToken(User user) {
        return buildToken(user, "2FA",
                properties.getTwoFactorMinutes(), ChronoUnit.MINUTES);
    }

    private String buildToken(User user, String type, long amount, ChronoUnit unit) {

        Instant now = Instant.now();

        return Jwts.builder()
                .issuer(properties.getIssuer())
                .audience().add(properties.getAudience()).and()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("type", type)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(amount, unit)))
                .signWith(key) // ✅ NO SignatureAlgorithm
                .compact();
    }

    // ================= CLAIM EXTRACTION =================

    public Long extractUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public String extractEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String extractType(String token) {
        return getClaims(token).get("type", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ================= PARSER =================

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)       // ✅ NEW API
                .build()
                .parseSignedClaims(token) // ✅ NEW API
                .getPayload();
    }
}
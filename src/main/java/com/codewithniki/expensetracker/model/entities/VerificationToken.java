package com.codewithniki.expensetracker.model.entities;

import com.codewithniki.expensetracker.model.enums.VerificationType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationType type;

    @Column(nullable = false)
    private boolean used = false;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public VerificationType getType() {
        return type;
    }

    public void setType(VerificationType type) {
        this.type = type;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
package com.codewithniki.expensetracker.repositories;

import com.codewithniki.expensetracker.model.entities.VerificationToken;
import com.codewithniki.expensetracker.model.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {


    Optional<VerificationToken> findByCodeAndTypeAndUsedFalse(
            String code,
            VerificationType type
    );
    void deleteAllByUser_IdAndType(Long userId, VerificationType type);

    void deleteAllByUser_Id(Long userId);
    Optional<VerificationToken>
    findTopByUser_IdAndTypeOrderByCreatedAtDesc(Long userId, VerificationType type);


}
package com.codewithniki.expensetracker.service.auth;

import com.codewithniki.expensetracker.exceptions.GlobalAppException;
import com.codewithniki.expensetracker.model.dtos.auth.*;
import com.codewithniki.expensetracker.model.entities.Role;
import com.codewithniki.expensetracker.model.entities.User;
import com.codewithniki.expensetracker.model.entities.VerificationToken;
import com.codewithniki.expensetracker.model.enums.VerificationType;
import com.codewithniki.expensetracker.repositories.RoleRepository;
import com.codewithniki.expensetracker.repositories.UserRepository;
import com.codewithniki.expensetracker.repositories.VerificationTokenRepository;
import com.codewithniki.expensetracker.service.jwt.JwtService;
import com.codewithniki.expensetracker.service.mail.MailService;
import com.codewithniki.expensetracker.service.sms.ISmsService;
import com.codewithniki.expensetracker.util.OtpGenerator;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final MailService mailService;
    private final JwtService jwtService;
    private final ISmsService smsService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            VerificationTokenRepository tokenRepository,
            MailService mailService,
            JwtService jwtService,
            ISmsService smsService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
        this.jwtService = jwtService;
        this.smsService = smsService;
    }

    // ================= REGISTER =================
    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new GlobalAppException("Email already registered");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new GlobalAppException("ROLE_USER not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(false);
        user.setTwoFactorEnabled(false);
        user.getRoles().add(userRole);

        userRepository.save(user);

        // Remove old tokens
        tokenRepository.deleteAllByUser_Id(user.getId());

        // Create email verification code
        String code = OtpGenerator.generate6Digits();

        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setCode(code);
        token.setType(VerificationType.EMAIL);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        token.setUsed(false);

        tokenRepository.save(token);

        mailService.sendVerificationEmail(user.getEmail(), code);

        return new RegisterResponse(
                "Registration successful. Please verify your email.",
                "/auth/verify-email"
        );
    }

    // ================= VERIFY EMAIL =================
    @Override
    @Transactional
    public void verifyEmail(String code) {

        VerificationToken token = tokenRepository
                .findByCodeAndTypeAndUsedFalse(code, VerificationType.EMAIL)
                .orElseThrow(() -> new GlobalAppException("Invalid or expired code"));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GlobalAppException("Verification code expired");
        }

        User user = token.getUser();
        user.setEmailVerified(true);
        token.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(token);
    }

    // ================= LOGIN =================
    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GlobalAppException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new GlobalAppException("Invalid credentials");
        }

        if (!user.isEmailVerified()) {
            throw new GlobalAppException("Email not verified");
        }

        // 🔐 2FA FLOW
        if (user.isTwoFactorEnabled()) {

            if (user.getPhoneNumber() == null) {
                throw new GlobalAppException("Phone number not set for 2FA");
            }

            String twoFactorToken = jwtService.generateTwoFactorToken(user);

            tokenRepository.deleteAllByUser_IdAndType(
                    user.getId(),
                    VerificationType.SMS
            );

            String code = OtpGenerator.generate6Digits();

            VerificationToken token = new VerificationToken();
            token.setUser(user);
            token.setCode(code);
            token.setType(VerificationType.SMS);
            token.setExpiresAt(LocalDateTime.now().plusMinutes(5));
            token.setUsed(false);

            tokenRepository.save(token);
            smsService.sendOtp(user.getPhoneNumber(), code);

            return new AuthResponse(
                    null,
                    null,
                    true,
                    twoFactorToken,
                    "OTP sent to your phone"
            );
        }

        // ✅ Normal login
        return new AuthResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                false,
                null,
                "Login successful"
        );
    }

    // ================= VERIFY 2FA =================
    @Override
    @Transactional
    public AuthResponse verifyTwoFactorLogin(String twoFactorToken, String code) {

        if (!jwtService.isTokenValid(twoFactorToken)
                || !"2FA".equals(jwtService.extractType(twoFactorToken))) {
            throw new GlobalAppException("Invalid 2FA token");
        }

        Long userId = jwtService.extractUserId(twoFactorToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalAppException("User not found"));

        VerificationToken token = tokenRepository
                .findByCodeAndTypeAndUsedFalse(code, VerificationType.SMS)
                .orElseThrow(() -> new GlobalAppException("Invalid code"));

        if (!token.getUser().getId().equals(userId)) {
            throw new GlobalAppException("Invalid user");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new GlobalAppException("Code expired");
        }

        token.setUsed(true);
        tokenRepository.save(token);

        return new AuthResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                false,
                null,
                "Login successful"
        );
    }
    // ================= RESEND EMAIL =================
    @Override
    @Transactional
    public void resendEmailVerification(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalAppException("User not found"));

        if (user.isEmailVerified()) {
            throw new GlobalAppException("Email already verified");
        }

        // Delete previous EMAIL tokens
        tokenRepository.deleteAllByUser_IdAndType(
                user.getId(),
                VerificationType.EMAIL
        );

        String code = OtpGenerator.generate6Digits();

        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setCode(code);
        token.setType(VerificationType.EMAIL);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        token.setUsed(false);

        tokenRepository.save(token);

        mailService.sendVerificationEmail(user.getEmail(), code);
    }
}
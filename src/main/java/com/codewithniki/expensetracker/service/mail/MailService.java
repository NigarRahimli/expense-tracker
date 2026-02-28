package com.codewithniki.expensetracker.service.mail;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String code) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your email");
        message.setText("""
                Welcome to ExpenseTrackerApp 👋
                
                Your verification code is:
                
                %s
                
                This code expires in 10 minutes.
                """.formatted(code));

        mailSender.send(message);
    }
}
package com.codewithniki.expensetracker.service.mail;

public interface IMailService {
    void sendVerificationEmail(String to, String code);
}
package com.codewithniki.expensetracker.service.sms;

public interface ISmsService {
    void sendOtp(String phoneNumber, String code);
}


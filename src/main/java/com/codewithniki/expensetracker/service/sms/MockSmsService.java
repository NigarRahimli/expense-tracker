package com.codewithniki.expensetracker.service.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev") // Only active in dev profile
public class MockSmsService implements ISmsService {

    private static final Logger log = LoggerFactory.getLogger(MockSmsService.class);

    @Override
    public void sendOtp(String phoneNumber, String code) {

        log.info("====== MOCK SMS ======");
        log.info("Sending OTP to: {}", phoneNumber);
        log.info("OTP Code: {}", code);
        log.info("======================");
    }
}

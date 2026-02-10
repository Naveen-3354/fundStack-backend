package com.test.FundStack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialNotificationService {

    private final JavaMailSender javaMailSender;

    public void sendCredentials(String email, String userId, String rawPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("FundStack Credentials");
            message.setText("Your FundStack credentials:\nuserId: " + userId + "\npassword: " + rawPassword);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.warn("Failed to send credential email to {}", email, e);
        }
    }
}

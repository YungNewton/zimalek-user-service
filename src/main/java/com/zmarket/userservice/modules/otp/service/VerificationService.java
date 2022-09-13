package com.zmarket.userservice.modules.otp.service;

public interface VerificationService {
    void startVerification(String recipient, String channel);
    boolean checkVerification(String recipient, String code);
}
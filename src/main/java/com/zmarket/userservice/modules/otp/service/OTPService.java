package com.zmarket.userservice.modules.otp.service;

import com.zmarket.userservice.modules.otp.enums.OTPChannel;
import com.zmarket.userservice.modules.security.model.User;

public interface OTPService {
    void sendOTP(User user, OTPChannel channel);
    boolean verifyOTP(String phoneOrEmail, String code);
}

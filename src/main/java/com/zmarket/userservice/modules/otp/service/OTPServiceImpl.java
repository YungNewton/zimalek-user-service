package com.zmarket.userservice.modules.otp.service;

import com.twilio.exception.ApiException;
import com.zmarket.userservice.modules.otp.enums.OTPChannel;
import com.zmarket.userservice.modules.security.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPServiceImpl implements  OTPService{

    private final TwilioVerification twilioVerification;

    @Override
    public synchronized void sendOTP(User user, OTPChannel channel){
        try {
            if (channel == OTPChannel.SMS) {
                twilioVerification.startVerification(user.getPhone(), "sms");
                return;
            }
            twilioVerification.startVerification(user.getEmail(), "email");


        }catch (ApiException ex){
            log.error("Twilio Error: {}", ex.getMessage());
        }

    }


    @Override
    public boolean verifyOTP(String phoneOrEmail, String code) {
        return  twilioVerification.checkVerification(phoneOrEmail, code);
    }
}

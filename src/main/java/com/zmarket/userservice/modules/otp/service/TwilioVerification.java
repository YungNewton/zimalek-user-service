package com.zmarket.userservice.modules.otp.service;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public class TwilioVerification implements VerificationService {
    public String verificationSID;

    public TwilioVerification(final String verificationSID){
        this.verificationSID = verificationSID;
    }
    public void startVerification(String recipient, String channel) {
        Verification.creator(
                verificationSID,
                recipient,
                channel)
                .create();
    }

    public boolean checkVerification(String recipient, String code) {
        VerificationCheck verificationCheck = VerificationCheck.creator(
                verificationSID)
                .setCode(code)
                .setTo(recipient).create();
        return "approved".equals(verificationCheck.getStatus());
    }
}
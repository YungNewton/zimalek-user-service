package com.zmarket.userservice.configs;

import com.twilio.Twilio;
import com.zmarket.userservice.modules.otp.service.TwilioVerification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioVerificationConfig {

    @Value("${twilio.account.sid}")
    public String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    public String AUTH_TOKEN;

    @Value("${twilio.verification.sid}")
    public String VERIFICATION_SID;

    @Bean
    public TwilioVerification initialize() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        return new TwilioVerification(VERIFICATION_SID);
    }
}
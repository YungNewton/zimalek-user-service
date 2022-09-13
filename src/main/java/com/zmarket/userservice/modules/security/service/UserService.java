package com.zmarket.userservice.modules.security.service;

import com.zmarket.userservice.dtos.BaseRequest;
import com.zmarket.userservice.modules.security.dto.LoginRequest;
import com.zmarket.userservice.modules.security.dto.LoginResponse;
import com.zmarket.userservice.modules.security.dto.RegistrationRequest;
import com.zmarket.userservice.modules.security.dto.RegistrationResponse;
import com.zmarket.userservice.modules.security.dto.ResetPasswordRequest;
import com.zmarket.userservice.modules.security.dto.SocialRequest;
import com.zmarket.userservice.modules.security.dto.UpdatePasswordRequest;
import com.zmarket.userservice.modules.security.dto.UpdateProfileRequest;
import com.zmarket.userservice.modules.security.model.User;

public interface UserService {
    RegistrationResponse registerUser(RegistrationRequest dto);

    Object resendCode(BaseRequest baseRequest);

    LoginResponse activateAccount(String otp, BaseRequest baseRequest);

    Object resetPassword(BaseRequest baseRequest);

    Object completeResetPassword(ResetPasswordRequest request);

    LoginResponse login(LoginRequest dto);

    User getUser();

    User updatePassword(UpdatePasswordRequest updatePinRequest);

    User updateProfile(UpdateProfileRequest request);

    LoginResponse registerUserWithGoogle(SocialRequest request);

    LoginResponse registerUserWithFacebook(SocialRequest request);

    LoginResponse loginGoogle(SocialRequest request);

    LoginResponse loginFacebook(SocialRequest request);
}

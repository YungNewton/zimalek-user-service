package com.zmarket.userservice.modules.security.controllers;

import com.zmarket.userservice.annotations.WrapApiResponse;
import com.zmarket.userservice.dtos.BaseRequest;
import com.zmarket.userservice.modules.security.dto.LoginRequest;
import com.zmarket.userservice.modules.security.dto.LoginResponse;
import com.zmarket.userservice.modules.security.dto.RegistrationRequest;
import com.zmarket.userservice.modules.security.dto.RegistrationResponse;
import com.zmarket.userservice.modules.security.dto.ResetPasswordRequest;
import com.zmarket.userservice.modules.security.dto.SocialRequest;
import com.zmarket.userservice.modules.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@WrapApiResponse
@RequiredArgsConstructor
public class AuthController {
   private final UserService userService;

   @PostMapping(value = "/register")
   public RegistrationResponse userRegistration(@RequestBody @Valid RegistrationRequest dto) {
      return userService.registerUser(dto);
   }

   @PostMapping(value = "/register/google")
   public LoginResponse userRegistrationWithGoogle(@RequestBody @Valid SocialRequest request) {
      return userService.registerUserWithGoogle(request);
   }

   @PostMapping(value = "/register/facebook")
   public LoginResponse userRegistrationWithFacebook(@RequestBody @Valid SocialRequest request) {
      return userService.registerUserWithFacebook(request);
   }
   @PostMapping(value = "/resend-code")
   public Object resendCode(@RequestBody @Valid BaseRequest baseRequest) {
      return userService.resendCode(baseRequest);
   }

   @PatchMapping(value = "/activate/{otp}")
   public LoginResponse userActivation(@PathVariable String otp, @RequestBody @Valid BaseRequest baseRequest) {
      return userService.activateAccount(otp, baseRequest);
   }

   @PostMapping("/login")
   public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
      return userService.login(loginRequest);
   }

   @PostMapping("/login/google")
   public LoginResponse loginGoogle(@Valid @RequestBody SocialRequest request) {
      return userService.loginGoogle(request);
   }

   @PostMapping("/login/facebook")
   public LoginResponse loginFacebook(@Valid @RequestBody SocialRequest request) {
      return userService.loginFacebook(request);
   }

   @PostMapping(value = "/reset-password")
   public Object resetPassword(@RequestBody @Valid BaseRequest baseRequest) {
      return userService.resetPassword(baseRequest);
   }

   @PatchMapping(value = "/reset-password")
   public Object completeResetPassword(@RequestBody @Valid ResetPasswordRequest request) {
      return userService.completeResetPassword(request);
   }

}

package com.zmarket.userservice.modules.security.dto;

import com.zmarket.userservice.annotations.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class ResetPasswordRequest {

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "otp is required")
    private String otp;

    @ValidPassword
    @NotBlank(message = "new password is required")
    private String newPassword;

}

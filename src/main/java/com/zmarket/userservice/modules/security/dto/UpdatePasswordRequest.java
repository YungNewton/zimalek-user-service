package com.zmarket.userservice.modules.security.dto;

import com.zmarket.userservice.annotations.ValidPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class UpdatePasswordRequest {
    @NotBlank(message = "old password is required")
    private String oldPassword;

    @ValidPassword
    @NotBlank(message = "new password is required")
    private String newPassword;

    @NotBlank(message = "confirm password is required")
    private String confirmPassword;
}

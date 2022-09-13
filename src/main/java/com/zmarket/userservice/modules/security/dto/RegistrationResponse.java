package com.zmarket.userservice.modules.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationResponse {
    private boolean isEmailVerified;
}

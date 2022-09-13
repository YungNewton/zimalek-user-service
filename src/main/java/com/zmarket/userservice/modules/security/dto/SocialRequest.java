package com.zmarket.userservice.modules.security.dto;

import com.zmarket.userservice.modules.security.enums.platform;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SocialRequest {
    @NotBlank(message = "access token is required")
    private String accessToken;

    @NotNull(message = "platform type is required")
    private platform platform;
}

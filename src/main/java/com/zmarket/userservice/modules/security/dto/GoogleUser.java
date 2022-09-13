package com.zmarket.userservice.modules.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUser {

    @JsonProperty("family_name")
    private String firstname;

    @JsonProperty("given_name")
    private String lastname;

    private String email;

    @JsonProperty("email_verified")
    private boolean isVerified;
}

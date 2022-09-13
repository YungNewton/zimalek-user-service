package com.zmarket.userservice.modules.security.dto;

import com.zmarket.userservice.modules.security.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private User user;
    private String token;
}

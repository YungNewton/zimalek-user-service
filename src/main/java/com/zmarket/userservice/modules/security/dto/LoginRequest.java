package com.zmarket.userservice.modules.security.dto;

import com.zmarket.userservice.modules.security.enums.platform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

   @NotBlank(message = "email is required")
   @Size(min = 1, max = 50)
   private String email;

   @NotNull(message = "password is required")
   @Size(min = 4, max = 100)
   private String password;

   private Boolean rememberMe;

   @NotNull(message = "platform type is required")
   private com.zmarket.userservice.modules.security.enums.platform platform;
}

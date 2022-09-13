package com.zmarket.userservice.modules.security.dto;

import com.zmarket.userservice.annotations.NoSpecialCharacter;
import com.zmarket.userservice.annotations.ValidPassword;
import com.zmarket.userservice.annotations.ValidPhoneNumber;
import com.zmarket.userservice.modules.security.enums.Gender;
import com.zmarket.userservice.modules.security.enums.platform;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ValidPhoneNumber(phone = "phoneNumber", countryCode = "countryCode", message = "Invalid Phone number")
@NoArgsConstructor
@Data
public class RegistrationRequest {

    @NoSpecialCharacter
    @NotBlank(message = "first name is required")
    private String firstName;

    @NoSpecialCharacter
    @NotBlank(message = "last name is required")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotNull(message = "platform type is required")
    private platform platform;

    private Gender gender;

    @ValidPassword
    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    private String countryCode = "NG";

}

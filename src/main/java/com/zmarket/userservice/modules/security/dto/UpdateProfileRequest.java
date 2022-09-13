package com.zmarket.userservice.modules.security.dto;

import com.zmarket.userservice.annotations.NoSpecialCharacter;
import com.zmarket.userservice.annotations.ValidPhoneNumber;
import com.zmarket.userservice.modules.security.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ValidPhoneNumber(phone = "phoneNumber", countryCode = "countryCode", message = "Invalid Phone number")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProfileRequest {


     @NoSpecialCharacter
     private String firstName;

     @NoSpecialCharacter
     private String lastName;

     private String phoneNumber;

     private String countryCode = "NG";

     private Gender gender;


}

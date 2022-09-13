package com.zmarket.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BaseRequest {

    @NotBlank(message = "email not found")
    private String email;
}

package com.zmarket.userservice.modules.security.controllers;

import com.zmarket.userservice.annotations.WrapApiResponse;
import com.zmarket.userservice.modules.security.dto.UpdatePasswordRequest;
import com.zmarket.userservice.modules.security.dto.UpdateProfileRequest;
import com.zmarket.userservice.modules.security.model.User;
import com.zmarket.userservice.modules.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
@WrapApiResponse
public class UserController {

    private final UserService userService;

    @PutMapping("/password")
    public User updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        return userService.updatePassword(request);
    }

    @PatchMapping()
    public User updateUser(@RequestBody @Valid UpdateProfileRequest request) {
        return userService.updateProfile(request);
    }


    @GetMapping
    public User getUser() {
        return userService.getUser();
    }

    //todo update profile firstname, lastname, phone
}

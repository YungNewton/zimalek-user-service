package com.zmarket.userservice.exceptions;

public class UnathorizedException extends RuntimeException {

    public UnathorizedException(String message) {
        super(message);
    }
}

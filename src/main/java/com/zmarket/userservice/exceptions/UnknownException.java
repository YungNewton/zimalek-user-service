package com.zmarket.userservice.exceptions;

public class UnknownException extends RuntimeException {
    public UnknownException(String message) {
        super(message);
    }
}

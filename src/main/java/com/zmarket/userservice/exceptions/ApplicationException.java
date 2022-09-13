package com.zmarket.userservice.exceptions;

public class ApplicationException extends RuntimeException{
    public ApplicationException(String message) {
        super(message);
    }
}

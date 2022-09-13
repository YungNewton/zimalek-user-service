package com.zmarket.userservice.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zmarket.userservice.dtos.ApiFieldError;
import com.zmarket.userservice.dtos.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class GenericControllerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex , new ApiResponse(false,  ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { BadRequestException.class, InterruptedException.class })
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex , new ApiResponse(false,  ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { UnknownException.class , Exception.class, RuntimeException.class})
    protected ResponseEntity<Object> handleUnknownException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiResponse(false,  ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { UnathorizedException.class, AuthenticationException.class})
    protected ResponseEntity<Object> handleUnauthorizedException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiResponse(false,  ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = { AccessDeniedException.class, ForbiddenException.class})
    protected ResponseEntity<Object> handleForbiddenException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiResponse(false,  ex.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { JsonProcessingException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleCircleException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex , new ApiResponse(false,  ex.getMessage()), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();

        List<ApiFieldError> error = bindingResult.getFieldErrors().stream().map(m -> new ApiFieldError(m.getField(), m.getDefaultMessage(), m.getRejectedValue())).collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponse<>(false, error), HttpStatus.BAD_REQUEST);
    }

}
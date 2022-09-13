package com.zmarket.userservice.configs.advice;

import com.zmarket.userservice.annotations.IgnoreWrapApiResponse;
import com.zmarket.userservice.annotations.WrapApiResponse;
import com.zmarket.userservice.dtos.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice(annotations = WrapApiResponse.class)
public class WrapApiResponseAdvice implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.getMethod() == null || (methodParameter.getMethod().getDeclaredAnnotation(IgnoreWrapApiResponse.class) == null);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return ApiResponse.builder()
                .responseMessage("SUCCESS")
                .requestSuccessful(true)
                .responseBody(body)
                .build();
    }
}

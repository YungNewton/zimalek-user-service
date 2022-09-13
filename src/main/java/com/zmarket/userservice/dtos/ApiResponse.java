package com.zmarket.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    private boolean requestSuccessful;
    private String responseMessage;
    private T responseBody;
    private List<ApiFieldError> errors;

    public ApiResponse(boolean requestSuccessful, String responseMessage) {
        this.requestSuccessful = requestSuccessful;
        this.responseMessage = responseMessage;
    }

    public ApiResponse(boolean requestSuccessful, List<ApiFieldError> errors) {
        this.requestSuccessful = requestSuccessful;
        this.errors = errors;
    }
}

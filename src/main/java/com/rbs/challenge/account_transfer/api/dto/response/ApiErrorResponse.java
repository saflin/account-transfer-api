package com.rbs.challenge.account_transfer.api.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.util.Arrays.asList;

@Data
public class ApiErrorResponse {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiErrorResponse(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiErrorResponse(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = asList(error);
    }
}

package com.rbs.challenge.account_transfer.api.controller;

import com.rbs.challenge.account_transfer.api.dto.response.ApiErrorResponse;
import com.rbs.challenge.account_transfer.exception.AccountNotFoundException;
import com.rbs.challenge.account_transfer.exception.InSufficientFundException;
import com.rbs.challenge.account_transfer.exception.InValidTransferRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(BAD_REQUEST, "Input validataion failure.", errors);
        return handleExceptionInternal(
                ex, apiErrorResponse, headers, apiErrorResponse.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(BAD_REQUEST, ex.getMessage(), EMPTY_LIST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(BAD_REQUEST, ex.getMessage(), errors);
        return new ResponseEntity<Object>(
                apiErrorResponse, new HttpHeaders(), apiErrorResponse.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiErrorResponse, new HttpHeaders(), apiErrorResponse.getStatus());
    }

    /**
     * Exception handler for bad requests
     *
     * @param request
     * @param exception
     * @return ResponseEntity
     */
    @ExceptionHandler({InSufficientFundException.class, AccountNotFoundException.class,
            InValidTransferRequestException.class})
    public ResponseEntity<?> handleClientSideExceptions(HttpServletRequest request, Exception exception) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<Object>(
                apiErrorResponse, new HttpHeaders(), apiErrorResponse.getStatus());
    }
}

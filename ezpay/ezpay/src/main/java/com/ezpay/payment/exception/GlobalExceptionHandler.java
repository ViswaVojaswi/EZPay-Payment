package com.ezpay.payment.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(
                    GlobalExceptionHandler.class
            );

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException(
            PaymentException exception) {

        log.warn(
                "Payment processing error: {}",
                exception.getMessage()
        );

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidationException(
            MethodArgumentNotValidException exception) {

        log.warn(
                "Request validation failed"
        );

        Map<String, String> errors =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>>
    handleGeneralException(Exception exception) {

        log.error(
                "Unexpected error occurred",
                exception
        );

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                "Internal server error"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
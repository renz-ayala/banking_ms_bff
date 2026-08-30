package com.banking.ms.bankingmsbff.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.InputMismatchException;
import java.util.Map;

@RestControllerAdvice
public class GloblaExceptionHandler {

    @ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<Map<String, String>> handleInvalidInput(InputMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "El valor ingresado no es válido"));
    }
}

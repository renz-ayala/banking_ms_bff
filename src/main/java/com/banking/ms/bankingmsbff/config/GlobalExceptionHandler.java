package com.banking.ms.bankingmsbff.config;

import com.banking.ms.bankingmsbff.util.Exceptions.BadCredentialsException;
import com.banking.ms.bankingmsbff.util.Exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return Mono.just(Map.of(
                "error", "Unauthorized",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Map<String, String>> handleNotFound(NotFoundException ex) {
        return Mono.just(Map.of(
                "error", "Sin data",
                "message", ex.getMessage()
        ));
    }
}

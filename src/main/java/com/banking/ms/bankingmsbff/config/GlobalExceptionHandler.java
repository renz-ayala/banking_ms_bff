package com.banking.ms.bankingmsbff.config;

import com.banking.ms.bankingmsbff.controller.dto.ExceptionResponse;
import com.banking.ms.bankingmsbff.util.Exceptions.BadCredentialsException;
import com.banking.ms.bankingmsbff.util.Exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<ExceptionResponse> handleBadCredentials(BadCredentialsException ex) {
        return Mono.just(
                new ExceptionResponse(
                        "Unauthorized",
                        ex.getMessage()
                )
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ExceptionResponse> handleNotFound(NotFoundException ex) {
        return Mono.just(
                new ExceptionResponse(
                        "Sin data",
                        ex.getMessage()
                )
        );
    }
}

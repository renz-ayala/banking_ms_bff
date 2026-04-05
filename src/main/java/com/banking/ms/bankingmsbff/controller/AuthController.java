package com.banking.ms.bankingmsbff.controller;

import com.banking.ms.bankingmsbff.service.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final CredentialService credentialService;

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String credentials) {
        return credentialService.generateToken(credentials)
                .map(token -> ResponseEntity.ok().body(token));
    }
}

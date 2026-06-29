package com.banking.ms.bankingmsbff.controller;

import com.banking.ms.bankingmsbff.service.CredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Obtener el JWT de autorización", description = "Devuelve el JWT usando las credenciales, se debe enviar en el formato de user:password en base64 en el header de autorizacion `Basic <credenciales>`")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "JWT generado exitosamente.", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "eyJjhGbIj...."))), @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Credenciales incorrectas\",\"error\": \"Unauthorized\"}")))})
    @GetMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String credentials) {
        return credentialService.generateToken(credentials)
                .map(token -> ResponseEntity.ok().body(token));
    }
}

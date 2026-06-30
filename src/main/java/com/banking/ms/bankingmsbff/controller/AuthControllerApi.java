package com.banking.ms.bankingmsbff.controller;

import com.banking.ms.bankingmsbff.controller.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

public interface AuthControllerApi {

    @Operation(
            summary = "Obtener el JWT de autorización",
            description = "Devuelve el JWT usando las credenciales, se debe enviar en el formato de user:password en base64 en el header de autorizacion `Basic <credenciales>`"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "JWT generado exitosamente.",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionResponse.class
                                    )
                            )
                    )
            }
    )
    Mono<ResponseEntity<String>> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String credentials);
}

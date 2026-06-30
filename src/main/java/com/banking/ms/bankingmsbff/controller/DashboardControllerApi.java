package com.banking.ms.bankingmsbff.controller;

import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.controller.dto.Container;
import com.banking.ms.bankingmsbff.controller.dto.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

public interface DashboardControllerApi {

    @Operation(
            summary = "Obtener la información completa del cliente junto a los productos registrados a su nombre",
            description = "Retorna los datos usando el cliente id, el cual debe ser enviado de forma encriptada"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Información recuperada exitosamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ClientDashboardDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El usuario no se halla en la lista de datos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionResponse.class
                                    )
                            )
                    )
            }
    )
    Mono<ClientDashboardDTO> getClientDashboard(@PathVariable String uniqueClientId, @RequestHeader("Authorization") String token);

    @Operation(
            summary = "Encriptación de datos",
            description = "encripta la inforamción ingresada"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Encriptación correcta",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    )
            }
            )
    String encryption(@PathVariable String data);

    @Operation(
            summary = "Desencriptación de datos",
            description = "desencripta la información ingresada"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Encriptación correcta",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No se puede desencriptar",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ExceptionResponse.class
                                    )
                            )
                    )
            }
    )
    String decryption(@PathVariable String data);

    @Operation(
            summary = "Codifica el dato a base64",
            description = "codifica el dato ingresado, debe ser un cadena de texto."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se ha codificado de forma correcta",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Container.class
                                    )
                            )
                    )
            }
    )
    Container toBase64(@RequestBody Container container);

    @Operation(
            summary = "Decodifica el dato ingresado a texto plano",
            description = "decodifica el dato, usando la cadena de texto ingresada."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Se ha decodificado de forma correcta",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Container.class
                                    )
                            )
                    )
            }
    )
    Container toString(@RequestBody Container container);
}

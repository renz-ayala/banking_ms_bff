package com.banking.ms.bankingmsbff.controller;

import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.service.DashboardService;
import com.banking.ms.bankingmsbff.util.Exceptions.BadCredentialsException;
import gg.renz.CryptUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/banca/dashboard")
public class DashboardController {

    private final CryptUtil cryptUtil;
    private final DashboardService dashboardService;

    @Operation(summary = "Obtener la información completa del cliente junto a los productos registrados a su nombre", description = "Retorna los datos usando el cliente id, el cual debe ser enviado de forma encriptada")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Información recuperada exitosamente.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDashboardDTO.class))), @ApiResponse(responseCode = "404", description = "El usuario no se halla en la lista de datos", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Sin data\",\"message\":\"No se halló al usuario\"}")))})
    @GetMapping("/get-data/{uniqueClientId}")
    public Mono<ClientDashboardDTO> getClientDashboard(@PathVariable String uniqueClientId, @RequestHeader("Authorization") String token) {
        return dashboardService.getClientDashboard(uniqueClientId, token);
    }

    @Operation(summary = "Encriptación de datos", description = "encripta la inforamción ingresada")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Encriptación correcta", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "p0kZh-ocUc7jU3p0O....")))})
    @GetMapping("/encryption/{data}")
    public String encryption(@PathVariable String data) {
        return cryptUtil.encrypt(data);
    }

    @Operation(summary = "Desencriptación de datos", description = "desencripta la inforamción ingresada")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Encriptación correcta", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Hola"))), @ApiResponse(responseCode = "401", description = "No se puede desencriptar", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\":\"Unauthorized\", \"message\":\"La información es incorrecta\"}")))})
    @GetMapping("/decryption/{data}")
    public String decryption(@PathVariable String data) {
        try {
            return cryptUtil.decrypt(data);
        } catch (Exception e) {
            log.error("Error encriptando", e);
            throw new BadCredentialsException("La información es incorrecta");
        }
    }
}

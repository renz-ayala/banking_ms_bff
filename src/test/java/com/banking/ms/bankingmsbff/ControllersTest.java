package com.banking.ms.bankingmsbff;

import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.controller.dto.Container;
import com.banking.ms.bankingmsbff.controller.implementation.AuthController;
import com.banking.ms.bankingmsbff.controller.implementation.DashboardController;
import com.banking.ms.bankingmsbff.service.CredentialService;
import com.banking.ms.bankingmsbff.service.DashboardService;
import com.banking.ms.bankingmsbff.service.model.Client;
import gg.renz.CryptUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = {AuthController.class, DashboardController.class}, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration.class
})
@AutoConfigureWebTestClient
class ControllersTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CredentialService credentialService;

    @MockitoBean
    private DashboardService dashboardService;

    @MockitoBean
    private CryptUtil cryptUtil;

    @Test
    void authControllerShouldReturnToken() {
        String mockHeader = "Basic dXNlcjpwYXNz";
        String mockJwt = "eyJhbGciOiJIUzI1NiJ9.mockToken";

        when(credentialService.generateToken(mockHeader)).thenReturn(Mono.just(mockJwt));

        webTestClient.get()
                .uri("/v1/login")
                .header(HttpHeaders.AUTHORIZATION, mockHeader)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                .expectBody(String.class).isEqualTo(mockJwt);

        verify(credentialService, times(1)).generateToken(mockHeader);
    }

    @Test
    void dashboardControllerShouldReturnClientData() {
        String uniqueId = "XYZ-123";
        String token = "Bearer unTokenCualquiera";

        Client mockClient = new Client();
        mockClient.setNames("Ruben Alberto");
        ClientDashboardDTO mockDto = ClientDashboardDTO.builder()
                .client(mockClient)
                .products(List.of())
                .build();

        when(dashboardService.getClientDashboard(uniqueId, token)).thenReturn(Mono.just(mockDto));

        webTestClient.get()
                .uri("/v1/banca/dashboard/get-data/{uniqueClientId}", uniqueId)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.client.names").isEqualTo("Ruben Alberto")
                .jsonPath("$.products").isArray();

        verify(dashboardService, times(1)).getClientDashboard(uniqueId, token);
    }

    @Test
    void dashboardControllerShouldEncryptData() {
        String rawData = "hola";
        String encryptedData = "base64UrlEncryptedString";

        when(cryptUtil.encrypt(rawData)).thenReturn(encryptedData);

        webTestClient.get()
                .uri("/v1/banca/dashboard/encryption/{data}", rawData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(encryptedData);
    }

    @Test
    void dashboardControllerShouldThrowExceptionWhenDecryptionFails() {
        String badData = "corruptData";

        when(cryptUtil.decrypt(badData)).thenThrow(new RuntimeException("AES Fail"));

        webTestClient.get()
                .uri("/v1/banca/dashboard/decryption/{data}", badData)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void dashboardControllerShouldEncodeToBase64() {
        Container input = new Container("hola");
        String expectedBase64 = Base64.getEncoder().encodeToString("hola".getBytes());

        webTestClient.post()
                .uri("/v1/banca/dashboard/base64/encode")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.text").isEqualTo(expectedBase64);
    }
}
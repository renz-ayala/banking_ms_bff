package com.banking.ms.bankingmsbff;

import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.service.implementation.DashboardServiceImpl;
import com.banking.ms.bankingmsbff.service.model.Client;
import com.banking.ms.bankingmsbff.service.model.Product;
import com.banking.ms.bankingmsbff.util.Exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardServiceImplTest {
    private static MockWebServer mockWebServer;
    private DashboardServiceImpl dashboardService;
    private ObjectMapper objectMapper;

    @BeforeAll
    static void startServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        WebClient webClient = WebClient
                .builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        dashboardService = new DashboardServiceImpl(webClient);

        ReflectionTestUtils.setField(dashboardService, "clientsUrl", "");
        ReflectionTestUtils.setField(dashboardService, "productsUrl", "");
    }

    @Test
    void shouldReturnDashboardSuccessfully() throws Exception {
        Client mockClient = new Client();
        mockClient.setNames("Ruben Alberto");

        Product mockProduct = new Product();
        mockProduct.setProductName("Cuenta Ahorros");

        final var dispatcher = new Dispatcher() {
            @Override
            public @NonNull MockResponse dispatch(@NonNull RecordedRequest request) throws InterruptedException {
                try {
                    if (request.getPath() == null) {
                        return new MockResponse().setResponseCode(400);
                    }

                    if (request.getPath().contains("/get-client/")) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setHeader("Content-Type", "application/json")
                                .setBody(objectMapper.writeValueAsString(mockClient));
                    } else if (request.getPath().contains("/get-products/")) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setHeader("Content-Type", "application/json")
                                .setBody(objectMapper.writeValueAsString(List.of(mockProduct)));
                    }
                } catch (Exception e) {
                    return new MockResponse().setResponseCode(500);
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        mockWebServer.setDispatcher(dispatcher);

        Mono<ClientDashboardDTO> result = dashboardService.getClientDashboard("XYZ-123", "Bearer token123");

        StepVerifier.create(result)
                .assertNext(dashboard -> {
                    assertNotNull(dashboard);
                    assertEquals("Ruben Alberto", dashboard.getClient().getNames());
                    assertEquals(1, dashboard.getProducts().size());
                    assertEquals("Cuenta Ahorros", dashboard.getProducts().get(0).getProductName());
                })
                .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenClientServiceFails() {
        final var errorDispatcher = new Dispatcher(){
            @Override
            public @NonNull MockResponse dispatch(@NonNull RecordedRequest request) {
                if (request.getPath() != null && request.getPath().contains("/get-client/")) {
                    return new MockResponse().setResponseCode(404);
                }
                return new MockResponse().setResponseCode(200).setBody("[]");
            }
        };

        mockWebServer.setDispatcher(errorDispatcher);

        Mono<ClientDashboardDTO> result = dashboardService.getClientDashboard("XYZ-123", "Bearer token123");

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException
                        && throwable.getMessage().equals("No se halló al usuario"))
                .verify();
    }
}
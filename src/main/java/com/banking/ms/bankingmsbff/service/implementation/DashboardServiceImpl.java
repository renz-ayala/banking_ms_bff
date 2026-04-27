package com.banking.ms.bankingmsbff.service.implementation;

import com.banking.ms.bankingmsbff.service.model.Client;
import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.service.model.Product;
import com.banking.ms.bankingmsbff.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final WebClient webClient;

    @Value("${services.clients}")
    private String clientsUrl;

    @Value("${services.products}")
    private String productsUrl;

    @Override
    public Mono<ClientDashboardDTO> getClientDashboard(String uniqueClientId) {

        Mono<Client> clientMono = webClient.get()
                .uri(clientsUrl + "/get-client/" + uniqueClientId)
                .retrieve().bodyToMono(Client.class)
                .timeout(Duration.ofSeconds(30))
                .onErrorResume(e -> {
                    log.error("Error getting client dashboard", e);
                    return Mono.error(new RuntimeException("No se encontró el cliente"));
                });

        Flux<Product> productsFlux = webClient.get()
                .uri(productsUrl + "/get-products/" + uniqueClientId)
                .retrieve().bodyToFlux(Product.class)
                .timeout(Duration.ofSeconds(30))
                .onErrorResume(e -> {
                    log.error("Error getting product dashboard", e);
                    return Flux.empty();
                });

        return Mono.zip(clientMono, productsFlux.collectList())
                .map(tuple -> ClientDashboardDTO.builder()
                        .client(tuple.getT1())
                        .products(tuple.getT2())
                        .build());

    }
}

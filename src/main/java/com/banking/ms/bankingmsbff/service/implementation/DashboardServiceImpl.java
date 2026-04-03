package com.banking.ms.bankingmsbff.service.implementation;

import com.banking.ms.bankingmsbff.model.Client;
import com.banking.ms.bankingmsbff.model.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.model.Product;
import com.banking.ms.bankingmsbff.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
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
                .retrieve().bodyToMono(Client.class);

        Flux<Product> productsFlux = webClient.get()
                .uri(productsUrl + "/get-products/" + uniqueClientId)
                .retrieve().bodyToFlux(Product.class);

        return Mono.zip(clientMono, productsFlux.collectList())
                .map(tuple -> ClientDashboardDTO.builder()
                        .client(tuple.getT1())
                        .products(tuple.getT2())
                        .build());

    }
}

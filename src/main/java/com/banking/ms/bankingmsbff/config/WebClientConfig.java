package com.banking.ms.bankingmsbff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .filter( (request, next) ->
                    Mono.deferContextual( context -> {
                        String id = context.getOrDefault(TrackingIdFilter.TRACK_ID, "No-Tracking-Id");
                        ClientRequest newRequest = ClientRequest.from(request).header(TrackingIdFilter.TRACK_ID, id).build();
                        return next.exchange(newRequest);
                    })
                )
                .build();
    }
}

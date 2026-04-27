package com.banking.ms.bankingmsbff.config;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.UUID;

@Component
public class TrackingIdFilter implements WebFilter {

    public static final String TRACK_ID = "X-Tracking-Id";

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String id = exchange.getRequest().getHeaders().getOrDefault(TRACK_ID, List.of(UUID.randomUUID().toString())).get(0);
        return chain.filter(exchange).contextWrite(Context.of(TRACK_ID, id));
    }
}

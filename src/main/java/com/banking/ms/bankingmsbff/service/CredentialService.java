package com.banking.ms.bankingmsbff.service;

import reactor.core.publisher.Mono;

public interface CredentialService {
    Mono<String> generateToken(String base64Credentials);
}

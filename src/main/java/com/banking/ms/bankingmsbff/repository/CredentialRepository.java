package com.banking.ms.bankingmsbff.repository;

import com.banking.ms.bankingmsbff.repository.entity.Credentials;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CredentialRepository extends R2dbcRepository<Credentials, Long> {
    Mono<Credentials> findByUsernameAndPassword(String username, String password);
}

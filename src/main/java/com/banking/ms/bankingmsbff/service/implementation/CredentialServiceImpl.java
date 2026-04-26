package com.banking.ms.bankingmsbff.service.implementation;

import com.banking.ms.bankingmsbff.model.Credentials;
import com.banking.ms.bankingmsbff.repository.CredentialRepository;
import com.banking.ms.bankingmsbff.service.CredentialService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    @Value("${secret_keys.codes.jwt}")
    private String secret;

    private final CredentialRepository repository;

    @Override
    public Mono<String> generateToken(String base64Credentials) {
        return Mono.just(base64Credentials)
                .map(header -> header.replace("Basic ",""))
                .map(encoded -> new String(Base64.getDecoder().decode(encoded)))
                .flatMap( decoded -> {
                    String[] parts = decoded.split(":", 2);
                    return repository.findByUsernameAndPassword(parts[0], parts[1]);
                })
                .map( user -> this.generateJwt(user.getUsername()))
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales incorrectas")));
    }

    private String generateJwt(String username) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}

package com.banking.ms.bankingmsbff;

import com.banking.ms.bankingmsbff.repository.CredentialRepository;
import com.banking.ms.bankingmsbff.repository.entity.Credentials;
import com.banking.ms.bankingmsbff.service.implementation.CredentialServiceImpl;
import com.banking.ms.bankingmsbff.util.Exceptions.BadCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Base64;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ServicesTest {

    @Mock
    private CredentialRepository repository;

    @InjectMocks
    private CredentialServiceImpl credentialService;

    private final String TEST_SECRET = "clavesecretadePruebasParaJWT123456789";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(credentialService, "secret", TEST_SECRET);
    }

    @Test
    void shouldGenerateTokenWhenCredentialsAreValid() {
        var user = "username";
        var password = "password";
        var format = String.format("%s:%s", user, password);
        var header = String.format("Basic %s", Base64.getEncoder().encodeToString(format.getBytes()));

        var userEntityMock = new Credentials();
        userEntityMock.setUsername(user);

        when(repository.findByUsernameAndPassword(user, password)).thenReturn(Mono.just(userEntityMock));

        Mono<String> token = credentialService.generateToken(header);

        StepVerifier
                .create(token)
                .assertNext( stringToken -> {
                    assertNotNull(stringToken);
                    assertTrue(stringToken.startsWith("eyJ"));
                })
                .verifyComplete();

        verify(repository, times(1)).findByUsernameAndPassword(user, password);
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        var user = "invalidUser";
        var pass = "invalidPassword";
        var credentials = String.format("%s:%s", user, pass);
        var header = String.format("Basic %s", Base64.getEncoder().encodeToString(credentials.getBytes()));

        when(repository.findByUsernameAndPassword(user, pass)).thenReturn(Mono.empty());

        Mono<String> tokenMono = credentialService.generateToken(header);

        StepVerifier
                .create(tokenMono)
                .expectErrorMatches(throwable -> throwable instanceof BadCredentialsException
                        && throwable.getMessage().equals("Credenciales incorrectas"))
                .verify();

        verify(repository, times(1)).findByUsernameAndPassword(user, pass);
    }
}

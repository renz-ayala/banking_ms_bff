package com.banking.ms.bankingmsbff.controller.implementation;

import com.banking.ms.bankingmsbff.controller.DashboardControllerApi;
import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.controller.dto.Container;
import com.banking.ms.bankingmsbff.service.DashboardService;
import gg.renz.CryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.InputMismatchException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/banca/dashboard")
public class DashboardController implements DashboardControllerApi {
    private final CryptUtil cryptUtil;
    private final DashboardService dashboardService;

    @Override
    @GetMapping("/get-data/{uniqueClientId}")
    public Mono<ClientDashboardDTO> getClientDashboard(@PathVariable String uniqueClientId, @RequestHeader("Authorization") String token) {
        return dashboardService.getClientDashboard(uniqueClientId, token);
    }

    @Override
    @GetMapping("/encryption/{data}")
    public String encryption(@PathVariable String data) {
        return cryptUtil.encrypt(data);
    }

    @Override
    @GetMapping("/decryption/{data}")
    public String decryption(@PathVariable String data) {
        try {
            return cryptUtil.decrypt(data);
        } catch (Exception e) {
            throw new InputMismatchException("La entrada es inválida");
        }

    }

    @Override
    @PostMapping("/base64/encode")
    public Container toBase64(@RequestBody Container container) {
        return new Container(new String(Base64.getEncoder().encode(container.text().getBytes())));
    }

    @Override
    @PostMapping("/base64/decode")
    public Container toString(@RequestBody Container container) {
        try {
            return new Container(new String(Base64.getDecoder().decode(container.text())));
        } catch (Exception e) {
            throw new InputMismatchException("La entrada es inválida");
        }
    }
}

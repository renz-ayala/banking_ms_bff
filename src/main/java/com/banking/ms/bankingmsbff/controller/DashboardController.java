package com.banking.ms.bankingmsbff.controller;

import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import com.banking.ms.bankingmsbff.service.DashboardService;
import com.banking.ms.bankingmsbff.util.CryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/banca/dashboard")
public class DashboardController {

    private final CryptUtil cryptUtil;
    private final DashboardService dashboardService;

    @GetMapping("/get-data/{uniqueClientId}")
    public Mono<ClientDashboardDTO> getClientDashboard(@PathVariable String uniqueClientId) {
        String finalUniqueClientId = cryptUtil.decrypt(uniqueClientId);
        return dashboardService.getClientDashboard(finalUniqueClientId);
    }

    @GetMapping("/encryption/{data}")
    public String encryption(@PathVariable String data) {
        return cryptUtil.encrypt(data);
    }
}

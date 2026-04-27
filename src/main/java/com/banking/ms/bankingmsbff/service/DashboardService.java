package com.banking.ms.bankingmsbff.service;

import com.banking.ms.bankingmsbff.controller.dto.ClientDashboardDTO;
import reactor.core.publisher.Mono;

public interface DashboardService {
    Mono<ClientDashboardDTO> getClientDashboard(String uniqueClientId);
}

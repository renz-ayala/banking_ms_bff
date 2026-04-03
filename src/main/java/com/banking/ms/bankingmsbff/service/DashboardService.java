package com.banking.ms.bankingmsbff.service;

import com.banking.ms.bankingmsbff.model.ClientDashboardDTO;
import reactor.core.publisher.Mono;

public interface DashboardService {
    Mono<ClientDashboardDTO> getClientDashboard(String uniqueClientId);
}

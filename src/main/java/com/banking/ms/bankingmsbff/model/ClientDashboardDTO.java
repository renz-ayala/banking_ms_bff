package com.banking.ms.bankingmsbff.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientDashboardDTO {
    private Client client;
    private List<Product> products;
}

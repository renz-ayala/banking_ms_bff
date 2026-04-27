package com.banking.ms.bankingmsbff.controller.dto;

import com.banking.ms.bankingmsbff.service.model.Client;
import com.banking.ms.bankingmsbff.service.model.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientDashboardDTO {
    private Client client;
    private List<Product> products;
}

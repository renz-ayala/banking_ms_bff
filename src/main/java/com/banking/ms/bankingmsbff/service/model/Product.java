package com.banking.ms.bankingmsbff.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String productType;
    private String productName;
    private Double balance;
}

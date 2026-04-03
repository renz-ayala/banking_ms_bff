package com.banking.ms.bankingmsbff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String names;
    private String surnames;
    private String documentType;
    private String documentNum;
}

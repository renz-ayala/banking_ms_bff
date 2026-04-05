package com.banking.ms.bankingmsbff.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "credentials", schema = "user1")
public class Credentials {
    @Id
    @Column("credential_id")
    private Long id;
    @Column("username")
    private String username;
    @Column("password")
    private String password;
}

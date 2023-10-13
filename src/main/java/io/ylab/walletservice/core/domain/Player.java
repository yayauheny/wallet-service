package io.ylab.walletservice.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player {

    private Long id;
    private PlayerRole role;
    private String username;
    private LocalDate birthDate;
    private byte[] hashedPassword;
    private Account account;
}

package io.ylab.walletservice.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private TransactionType type;
    private BigDecimal amount;
    private String currencyCode;
    private Long participantAccountId;
    private Currency currency;
    private Account participantAccount;

}

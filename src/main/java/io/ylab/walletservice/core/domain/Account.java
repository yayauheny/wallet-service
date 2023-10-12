package io.ylab.walletservice.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private Long id;
    @Builder.Default
    private BigDecimal currentBalance = BigDecimal.ZERO;
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
    private Currency currency;
    private Long playerId;
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();
}

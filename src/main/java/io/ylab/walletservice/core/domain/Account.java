package io.ylab.walletservice.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(exclude = "transactions")
@Getter
@Setter
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

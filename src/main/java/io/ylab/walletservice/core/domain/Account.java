package io.ylab.walletservice.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(exclude = "transactions")
@Getter
@Setter
@ToString(exclude = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private Long id;
    @Builder.Default
    private BigDecimal currentBalance = BigDecimal.ZERO;
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
    private String currencyCode;
    private Long playerId;
    private Currency currency;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

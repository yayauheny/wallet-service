package io.ylab.walletservice.core.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountUpdateDto(Long id,
                               @JsonProperty("currency_code")
                               String currencyCode,
                               @JsonProperty("current_balance")
                               BigDecimal currentBalance,
                               @JsonProperty("player_id")
                               Long playerId) {

}

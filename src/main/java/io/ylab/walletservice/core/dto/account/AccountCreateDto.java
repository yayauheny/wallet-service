package io.ylab.walletservice.core.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountCreateDto(Long id,
                               @JsonProperty("currency_code")
                               String currencyCode,
                               @JsonProperty("current_balance")
                               BigDecimal currentBalance,
                               @JsonProperty("player_id")
                               Long playerId,
                               @JsonProperty("created_at")
                               LocalDate createdAt) {

    @JsonCreator
    public static AccountCreateDto create(
            Long id,
            @JsonProperty("currency_code")
            String currencyCode,
            @JsonProperty("current_balance")
            BigDecimal currentBalance,
            @JsonProperty("player_id")
            Long playerId,
            @JsonProperty("created_at")
            LocalDate createdAt) {
        LocalDate defaultTime = (createdAt != null) ? createdAt : LocalDate.now();
        return new AccountCreateDto(id, currencyCode, currentBalance, playerId, defaultTime);
    }
}

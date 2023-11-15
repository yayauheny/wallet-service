package io.ylab.walletservice.core.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.dto.player.PlayerResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(Long id,
                              @JsonProperty("current_balance")
                              BigDecimal currentBalance,
                              @JsonProperty("created_at")
                              LocalDateTime createdAt,
                              Currency currency,
                              PlayerResponse player) {

}

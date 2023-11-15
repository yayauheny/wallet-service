package io.ylab.walletservice.core.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ylab.walletservice.core.domain.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(Long id,
                                  BigDecimal amount,
                                  String type,
                                  Currency currency,
                                  @JsonProperty("created_at")
                                  LocalDateTime createdAt,
                                  @JsonProperty("participant_account_id")
                                  Long participantAccountId) {

}

package io.ylab.walletservice.core.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record TransactionRequest(BigDecimal amount,
                                 String type,
                                 @JsonProperty("currency_code")
                                 String currencyCode,
                                 @JsonProperty("participant_account_id")
                                 Long participantAccountId) {

}

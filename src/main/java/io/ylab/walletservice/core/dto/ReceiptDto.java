package io.ylab.walletservice.core.dto;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;

import java.time.LocalDateTime;

public record ReceiptDto(Account account,
                         Player player,
                         LocalDateTime from,
                         LocalDateTime to) {
}

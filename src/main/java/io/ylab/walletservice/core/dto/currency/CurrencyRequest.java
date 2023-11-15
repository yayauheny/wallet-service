package io.ylab.walletservice.core.dto.currency;

import java.math.BigDecimal;

public record CurrencyRequest(String code,
                              BigDecimal rate) {

}

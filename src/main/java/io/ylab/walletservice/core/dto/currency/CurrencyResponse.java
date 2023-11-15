package io.ylab.walletservice.core.dto.currency;

import java.math.BigDecimal;

public record CurrencyResponse(Long id,
                               String code,
                               BigDecimal rate) {

}

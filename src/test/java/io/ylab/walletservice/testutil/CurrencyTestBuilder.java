package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCurrency")
@With
public class CurrencyTestBuilder implements TestBuilder<Currency> {

    private Long id = 0L;
    private String code;
    private BigDecimal rate = BigDecimal.ONE;

    @Override
    public Currency build() {
        return Currency.builder()
                .id(id)
                .code(code)
                .rate(rate)
                .build();
    }
}
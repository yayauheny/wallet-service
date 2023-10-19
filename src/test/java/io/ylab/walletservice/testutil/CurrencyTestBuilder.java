package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

/**
 * The {@link CurrencyTestBuilder} class is a test utility for creating instances of the {@link Currency} class
 * with customizable or default attribute values. It implements the {@link TestBuilder} interface to provide
 * a standardized way of constructing {@link Currency} objects for testing scenarios.
 */
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
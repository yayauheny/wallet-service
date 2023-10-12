package io.ylab.walletservice.repository.impl;

import io.ylab.walletservice.core.repository.impl.CurrencyRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import io.ylab.walletservice.core.domain.Currency;

import java.math.BigDecimal;


@DisplayName("AccountRepositoryImpl Tests")
class CurrencyRepositoryImplTest {

    CurrencyRepositoryImpl currencyRepository;






    private Currency createTestAccount() {
        return Currency.builder()
                .code("USD")
                .rate(BigDecimal.ONE)
                .build();
    }
}
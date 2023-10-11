package yayauheny.repository.impl;

import org.junit.jupiter.api.DisplayName;
import yayauheny.entity.Currency;

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
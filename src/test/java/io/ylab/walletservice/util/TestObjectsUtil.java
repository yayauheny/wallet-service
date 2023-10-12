package io.ylab.walletservice.util;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;

import java.math.BigDecimal;


public class TestObjectsUtil {

    public static final Account TEST_ACCOUNT = AccountTestBuilder.anAccount()
            .withId(1L)
            .withPlayerId(1L)
            .withCurrentBalance(new BigDecimal("100"))
            .build();

    public static final Currency TEST_CURRENCY = CurrencyTestBuilder.aCurrency()
            .withId(1L)
            .withCode("USD")
            .withRate(BigDecimal.ONE)
            .build();
}

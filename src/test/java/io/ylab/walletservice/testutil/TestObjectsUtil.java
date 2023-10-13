package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class TestObjectsUtil {

    public static final Account TEST_ACCOUNT = AccountTestBuilder.anAccount()
            .withId(1L)
            .withPlayerId(1L)
            .withCurrentBalance(BigDecimal.ZERO)
            .build();
    public static final Currency TEST_CURRENCY = CurrencyTestBuilder.aCurrency()
            .withId(1L)
            .withCode("USD")
            .withRate(BigDecimal.ONE)
            .build();
    public static final Player TEST_PLAYER = PlayerTestBuilder.aPlayer()
            .withId(1L)
            .withUsername("ivan")
            .withBirthDate(LocalDate.of(1995, 12, 10))
            .build();
    public static final Transaction TEST_TRANSACTION = TransactionTestBuilder.aTransaction()
            .withId(1L)
            .withAmount(new BigDecimal("200"))
            .withType(TransactionType.CREDIT)
            .build();
}

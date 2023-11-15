package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.PlayerRole;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.core.service.AccountService;
import io.ylab.walletservice.core.service.CurrencyService;
import io.ylab.walletservice.core.service.PlayerService;
import io.ylab.walletservice.exception.DatabaseException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The {@link TestObjectsUtil} class provides a set of predefined test objects for various domain entities,
 * facilitating the setup of consistent and reusable test scenarios.
 */
@UtilityClass
public class TestObjectsUtil {

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, 1, 1, 12, 0);
    public static final Player TEST_PLAYER_IVAN = PlayerTestBuilder.aPlayer()
            .withUsername("ivan53")
            .withBirthDate(LocalDate.of(1990, 1, 15))
            .withRole(PlayerRole.USER)
            .withPassword("test")
            .build();
    public static final Player TEST_PLAYER_ANDREW = PlayerTestBuilder.aPlayer()
            .withUsername("andrew223")
            .withBirthDate(LocalDate.of(2003, 9, 13))
            .withRole(PlayerRole.USER)
            .withPassword("test")
            .build();
    public static final Player TEST_PLAYER_HANNA = PlayerTestBuilder.aPlayer()
            .withUsername("hanna-admin")
            .withBirthDate(LocalDate.of(2004, 2, 28))
            .withRole(PlayerRole.ADMIN)
            .withPassword("test")
            .build();
    public static final Account TEST_ACCOUNT_IVAN = AccountTestBuilder.anAccount()
            .withCurrencyCode("USD")
            .withCurrentBalance(new BigDecimal("500.00"))
            .withCreatedAt(LOCAL_DATE_TIME)
            .build();
    public static final Account TEST_ACCOUNT_ANDREW = AccountTestBuilder.anAccount()
            .withCurrencyCode("EUR")
            .withCurrentBalance(new BigDecimal("0.00"))
            .withCreatedAt(LOCAL_DATE_TIME)
            .build();
    public static final Account TEST_ACCOUNT_HANNA = AccountTestBuilder.anAccount()
            .withCurrencyCode("JPY")
            .withCurrentBalance(new BigDecimal("0.00"))
            .withCreatedAt(LOCAL_DATE_TIME)
            .build();
    public static final Currency TEST_CURRENCY_USD = CurrencyTestBuilder.aCurrency()
            .withCode("USD")
            .withRate(new BigDecimal("1.00"))
            .build();
    public static final Currency TEST_CURRENCY_EUR = CurrencyTestBuilder.aCurrency()
            .withCode("EUR")
            .withRate(new BigDecimal("1.27"))
            .build();
    public static final Currency TEST_CURRENCY_JPY = CurrencyTestBuilder.aCurrency()
            .withCode("JPY")
            .withRate(new BigDecimal("110.50"))
            .build();
    public static final Transaction TEST_TRANSACTION = TransactionTestBuilder.aTransaction()
            .withAmount(new BigDecimal("200.00"))
            .withType(TransactionType.CREDIT)
            .withCreatedAt(LOCAL_DATE_TIME)
            .withCurrencyCode("USD")
            .withParticipantAccount(TEST_ACCOUNT_IVAN)
            .build();
    public static final Transaction TEST_TRANSACTION_CREDIT = TransactionTestBuilder.aTransaction()
            .withAmount(new BigDecimal("600.00"))
            .withType(TransactionType.CREDIT)
            .withCreatedAt(LOCAL_DATE_TIME)
            .withCurrencyCode("EUR")
            .withParticipantAccountId(TEST_ACCOUNT_HANNA.getId())
            .build();

    public static void createObjects(CurrencyService currencyService, PlayerService playerService, AccountService accountService) {
        try {
            currencyService.save(TEST_CURRENCY_USD);
            currencyService.save(TEST_CURRENCY_EUR);
            currencyService.save(TEST_CURRENCY_JPY);

//            playerService.save(TEST_PLAYER_IVAN);
//            playerService.save(TEST_PLAYER_ANDREW);
//            playerService.save(TEST_PLAYER_HANNA);

            TEST_ACCOUNT_IVAN.setPlayerId(TEST_PLAYER_IVAN.getId());
            TEST_ACCOUNT_ANDREW.setPlayerId(TEST_PLAYER_ANDREW.getId());
            TEST_ACCOUNT_HANNA.setPlayerId(TEST_PLAYER_HANNA.getId());

            accountService.save(TEST_ACCOUNT_IVAN);
            accountService.save(TEST_ACCOUNT_ANDREW);
            accountService.save(TEST_ACCOUNT_HANNA);

            TEST_PLAYER_IVAN.setAccount(TEST_ACCOUNT_IVAN);
            TEST_PLAYER_ANDREW.setAccount(TEST_ACCOUNT_ANDREW);
            TEST_PLAYER_HANNA.setAccount(TEST_ACCOUNT_HANNA);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}



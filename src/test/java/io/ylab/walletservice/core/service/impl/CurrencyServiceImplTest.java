package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.testutil.CurrencyTestBuilder;
import io.ylab.walletservice.testutil.PostgresTestcontainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static io.ylab.walletservice.testutil.TestObjectsUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    static CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
    static PlayerServiceImpl playerService = new PlayerServiceImpl();
    static AccountServiceImpl accountService = new AccountServiceImpl();

    @BeforeAll
    static void loadDatabase() {
        PostgresTestcontainer.init();
        createObjects(currencyService, playerService, accountService);
    }

    @AfterAll
    static void close() {
        PostgresTestcontainer.close();
    }

    @Test
    @DisplayName("should find existing currency by id")
    void shouldFindCurrencyById() throws DatabaseException {
        Long currencyId = TEST_CURRENCY_USD.getId();
        Optional<Currency> expectedCurrency = Optional.of(TEST_CURRENCY_USD);
        Optional<Currency> actualResult = currencyService.findById(currencyId);

        assertThat(actualResult).isPresent().isEqualTo(expectedCurrency);
    }

    @Test
    @DisplayName("should return empty optional")
    void shouldReturnEmptyOptionalWhenCurrencyNotFound() throws DatabaseException {
        Optional<Currency> actualResult = currencyService.findById(999L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should throw an exception if id is null or negative")
    void shouldThrowInvalidIdExceptionIfIdIsInvalid() {
        Long emptyId = null;
        Long negativeId = -500L;

        assertThrows(InvalidIdException.class, () -> currencyService.findById(emptyId));
        assertThrows(InvalidIdException.class, () -> currencyService.findById(negativeId));
    }

    @Test
    @DisplayName("should return all currencies")
    void shouldReturnAllCurrencies() throws DatabaseException {
        int expectedSize = 3;
        List<Currency> actualResult = currencyService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_CURRENCY_USD, TEST_CURRENCY_EUR, TEST_CURRENCY_JPY);
    }

    @Test
    @DisplayName("should save currency")
    void shouldSaveCurrencyCorrectly() throws DatabaseException {
        Currency currency = CurrencyTestBuilder.aCurrency()
                .withCode("BYN")
                .withRate(new BigDecimal("1.00"))
                .build();

        Currency savedCurrency = currencyService.save(currency);

        assertThat(savedCurrency).isNotNull().isEqualTo(currency);
    }

    @Test
    @DisplayName("should update existing currency")
    void shouldUpdateExistingCurrency() throws DatabaseException {
        TEST_CURRENCY_USD.setRate(new BigDecimal("5555.00"));
        currencyService.update(TEST_CURRENCY_USD);

        Optional<Currency> actualResult = currencyService.findById(TEST_CURRENCY_USD.getId());

        assertThat(actualResult).isPresent().isEqualTo(Optional.of(TEST_CURRENCY_USD));
    }

    @Test
    @DisplayName("should delete currency")
    void shouldDeleteCurrency() throws DatabaseException {
        Currency currency = CurrencyTestBuilder.aCurrency()
                .withRate(new BigDecimal("123123.55"))
                .withCode("III")
                .build();

        Currency savedCurrency = currencyService.save(currency);
        currencyService.delete(currency);
        Optional<Currency> emptyCurrency = currencyService.findById(currency.getId());

        assertThat(savedCurrency).isNotNull().isEqualTo(currency);
        assertThat(emptyCurrency).isEmpty();
    }

}
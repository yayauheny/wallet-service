package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.repository.impl.CurrencyRepositoryImpl;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.testutil.CurrencyTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_CURRENCY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @InjectMocks
    CurrencyServiceImpl currencyService;
    @Mock
    CurrencyRepositoryImpl currencyRepository;

    @Test
    @DisplayName("should find existing currency by id")
    void shouldFindCurrencyById() {
        Long currencyId = TEST_CURRENCY.getId();
        Optional<Currency> expectedCurrency = Optional.of(TEST_CURRENCY);

        doReturn(expectedCurrency)
                .when(currencyRepository).findById(currencyId);

        Optional<Currency> actualResult = currencyService.findById(currencyId);

        assertThat(actualResult).isPresent().isEqualTo(expectedCurrency);
    }

    @Test
    @DisplayName("should return empty optional")
    void shouldReturnEmptyOptionalWhenCurrencyNotFound() {
        Optional<Currency> expectedCurrency = Optional.empty();

        doReturn(expectedCurrency)
                .when(currencyRepository).findById(any());

        Optional<Currency> actualResult = currencyService.findById(0L);

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
    void shouldReturnAllCurrencies() {
        int expectedSize = 1;

        doReturn(List.of(TEST_CURRENCY))
                .when(currencyRepository).findAll();

        List<Currency> actualResult = currencyService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_CURRENCY);
    }

    @Test
    @DisplayName("should return an empty list")
    void shouldReturnAnEmptyListIfNoCurrenciesFound() {
        doReturn(List.of())
                .when(currencyRepository).findAll();

        List<Currency> actualResult = currencyService.findAll();

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should save currency")
    void shouldSaveCurrencyCorrectly() {
        doReturn(TEST_CURRENCY)
                .when(currencyRepository).save(TEST_CURRENCY);

        Currency actualResult = currencyService.save(TEST_CURRENCY);

        assertThat(actualResult).isNotNull().isEqualTo(TEST_CURRENCY);
    }

    @Test
    @DisplayName("should update existing currency")
    void shouldUpdateExistingCurrency() {
        Currency currency = CurrencyTestBuilder.aCurrency()
                .withId(100L)
                .withCode("EUR")
                .build();
        Currency updatedCurrency = CurrencyTestBuilder.aCurrency()
                .withId(currency.getId())
                .withCode("PLN")
                .build();

        doReturn(currency)
                .when(currencyRepository).save(currency);

        currencyService.save(currency);
        currencyService.update(updatedCurrency);

        doReturn(Optional.of(updatedCurrency))
                .when(currencyRepository).findById(updatedCurrency.getId());

        Optional<Currency> actualResult = currencyService.findById(updatedCurrency.getId());

        assertThat(actualResult).isPresent().isEqualTo(Optional.of(updatedCurrency));
    }
}
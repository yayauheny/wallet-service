package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.exception.InvalidFundsException;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.testutil.AccountTestBuilder;
import io.ylab.walletservice.testutil.PostgresTestcontainer;
import io.ylab.walletservice.testutil.PostgresTestcontainer.*;
import io.ylab.walletservice.testutil.TestObjectsUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.ylab.walletservice.testutil.TestObjectsUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    static CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
    static PlayerServiceImpl playerService = new PlayerServiceImpl();
    static AccountServiceImpl accountService = new AccountServiceImpl();

    @BeforeAll
    static void startContainer() {
        PostgresTestcontainer.init();
        TestObjectsUtil.createObjects(currencyService, playerService, accountService);
    }

    @AfterAll
    static void closeContainer() {
        PostgresTestcontainer.close();
    }

    @Test
    @DisplayName("should find existing account by id")
    void shouldFindAccountById() throws DatabaseException {
        Long accountId = TEST_ACCOUNT_IVAN.getId();
        Optional<Account> expectedAccount = Optional.of(TEST_ACCOUNT_IVAN);

        Optional<Account> actualResult = accountService.findById(accountId);

        assertThat(actualResult).isPresent().isEqualTo(expectedAccount);
    }

    @Test
    @DisplayName("should return empty optional")
    void shouldReturnEmptyOptionalWhenAccountNotFound() throws DatabaseException {
        Optional<Account> actualResult = accountService.findById(555L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should throw an exception if id is null or negative")
    void shouldThrowInvalidIdExceptionIfIdIsInvalid() {
        Long emptyId = null;
        Long negativeId = -500L;

        assertThrows(InvalidIdException.class, () -> accountService.findById(emptyId));
        assertThrows(InvalidIdException.class, () -> accountService.findById(negativeId));
    }

    @Test
    @DisplayName("should find account by player id")
    void shouldFindAccountByPlayerId() throws DatabaseException {
        Long playerId = TEST_ACCOUNT_HANNA.getPlayerId();
        Optional<Account> expectedAccount = Optional.of(TEST_ACCOUNT_HANNA);

        Optional<Account> actualResult = accountService.findByPlayerId(playerId);

        assertThat(actualResult).isPresent().isEqualTo(expectedAccount);
    }

    @Test
    @DisplayName("should return all accounts")
    void shouldReturnAllAccounts() throws DatabaseException {
        int expectedSize = 3;

        List<Account> actualResult = accountService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_ACCOUNT_IVAN, TEST_ACCOUNT_ANDREW, TEST_ACCOUNT_HANNA);
    }

    @Test
    @DisplayName("should save and return saved account")
    void shouldSaveAndReturnAccount() throws DatabaseException {
        Account account = AccountTestBuilder.anAccount()
                .withCurrencyCode("USD")
                .withCurrentBalance(BigDecimal.ZERO)
                .withCreatedAt(LocalDateTime.now())
                .withPlayerId(3L)
                .build();
        Account actualResult = accountService.save(account);

        assertThat(actualResult).isNotNull().isEqualTo(account);
    }

    @Test
    @DisplayName("should update existing account")
    void shouldUpdateExistingAccount() throws DatabaseException {
        TEST_ACCOUNT_IVAN.setCurrentBalance(new BigDecimal("900.00"));
        accountService.update(TEST_ACCOUNT_IVAN);

        Optional<Account> actualResult = accountService.findById(TEST_ACCOUNT_IVAN.getId());

        assertThat(actualResult).isPresent().isEqualTo(Optional.of(TEST_ACCOUNT_IVAN));
    }

    @Test
    @DisplayName("should update balance correctly")
    void shouldUpdateBalanceCorrectly() throws DatabaseException {
        Optional<Account> expected = Optional.of(TEST_ACCOUNT_IVAN);
        BigDecimal updatedBalance = TEST_ACCOUNT_IVAN.getCurrentBalance()
                .add(new BigDecimal("600.00"));

        accountService.updateBalance(TEST_ACCOUNT_IVAN, updatedBalance);
        Optional<Account> actualResult = accountService.findById(TEST_ACCOUNT_IVAN.getId());

        assertThat(actualResult).isPresent().isEqualTo(expected);
        assertThat(TEST_ACCOUNT_IVAN.getCurrentBalance()).isEqualTo(updatedBalance);
    }

    @Test
    @DisplayName("should throw exception when balance incorrect")
    void shouldThrowInvalidFundsExceptionWhenBalanceIncorrect() throws DatabaseException {
        Account account = AccountTestBuilder.anAccount()
                .withPlayerId(TEST_PLAYER_IVAN.getId())
                .withCurrentBalance(BigDecimal.ZERO)
                .withCreatedAt(LocalDateTime.now())
                .build();
        BigDecimal updatedBalance = account.getCurrentBalance().add(new BigDecimal("-500.00"));

        Account savedAccount = accountService.save(account);
        assertThat(savedAccount).isNotNull().isEqualTo(account);
        assertThrows(InvalidFundsException.class, () -> accountService.updateBalance(account, updatedBalance));
    }

    @Test
    @DisplayName("should delete account")
    void shouldDeleteAccount() throws DatabaseException {
        Long accountId = TEST_ACCOUNT_ANDREW.getId();
        boolean deleted = accountService.delete(TEST_ACCOUNT_ANDREW);
        Optional<Account> actualResult = accountService.findById(accountId);

        assertThat(deleted).isTrue();
        assertThat(actualResult).isEmpty();
    }
}
package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.repository.impl.AccountRepositoryImpl;
import io.ylab.walletservice.exception.InvalidFundsException;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.testutil.AccountTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_ACCOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    AccountServiceImpl accountService;
    @Mock
    AccountRepositoryImpl accountRepository;

    @Test
    @DisplayName("should find existing account by id")
    void shouldFindAccountById() {
        Long accountId = TEST_ACCOUNT.getId();
        Optional<Account> expectedAccount = Optional.of(TEST_ACCOUNT);

        doReturn(expectedAccount)
                .when(accountRepository).findById(accountId);

        Optional<Account> actualResult = accountService.findById(accountId);

        assertThat(actualResult).isPresent().isEqualTo(expectedAccount);
    }

    @Test
    @DisplayName("should return empty optional")
    void shouldReturnEmptyOptionalWhenAccountNotFound() {
        Optional<Account> expectedAccount = Optional.empty();

        doReturn(expectedAccount)
                .when(accountRepository).findById(any());

        Optional<Account> actualResult = accountService.findById(0L);

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
    void shouldFindAccountByPlayerId() {
        Long playerId = TEST_ACCOUNT.getPlayerId();
        Optional<Account> expectedAccount = Optional.of(TEST_ACCOUNT);

        doReturn(expectedAccount)
                .when(accountRepository)
                .findByPlayerId(playerId);

        Optional<Account> actualResult = accountService.findByPlayerId(playerId);

        assertThat(actualResult).isPresent().isEqualTo(expectedAccount);
    }

    @Test
    @DisplayName("should return all accounts")
    void shouldReturnAllAccounts() {
        int expectedSize = 1;

        doReturn(List.of(TEST_ACCOUNT))
                .when(accountRepository).findAll();

        List<Account> actualResult = accountService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_ACCOUNT);
    }

    @Test
    @DisplayName("should return an empty list if no accounts found")
    void shouldReturnAnEmptyListIfNoAccountsFound() {
        doReturn(List.of())
                .when(accountRepository).findAll();

        List<Account> actualResult = accountService.findAll();

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should save and return saved account")
    void shouldSaveAndReturnAccount() {
        doReturn(TEST_ACCOUNT)
                .when(accountRepository).save(TEST_ACCOUNT);

        Account actualResult = accountService.save(TEST_ACCOUNT);

        assertThat(actualResult).isNotNull().isEqualTo(TEST_ACCOUNT);
    }

    @Test
    @DisplayName("should update existing account")
    void shouldUpdateExistingAccount() {
        Account account = AccountTestBuilder.anAccount()
                .withId(100L)
                .withPlayerId(100L)
                .build();
        Account updatedAccount = AccountTestBuilder.anAccount()
                .withId(account.getId())
                .withPlayerId(account.getPlayerId())
                .withCurrentBalance(new BigDecimal("500"))
                .build();
        Optional<Account> expected = Optional.of(updatedAccount);

        doReturn(account)
                .when(accountRepository).save(account);

        accountService.save(account);
        accountService.update(updatedAccount);

        doReturn(expected)
                .when(accountRepository).findById(updatedAccount.getId());

        Optional<Account> actualResult = accountService.findById(updatedAccount.getId());

        assertThat(actualResult).isPresent().isEqualTo(expected);
    }

    @Test
    @DisplayName("should update balance correctly")
    void shouldUpdateBalanceCorrectly() {
        Account account = AccountTestBuilder.anAccount()
                .withId(100L)
                .withPlayerId(100L)
                .withCurrentBalance(BigDecimal.ZERO)
                .build();
        BigDecimal updatedBalance = account.getCurrentBalance()
                .add(new BigDecimal("600"));

        doReturn(account)
                .when(accountRepository).save(account);

        Account actualResult = accountService.save(account);
        accountService.updateBalance(actualResult, updatedBalance);

        assertThat(actualResult).isNotNull().isEqualTo(account);
        assertThat(actualResult.getCurrentBalance()).isEqualTo(updatedBalance.toString());
    }

    @Test
    @DisplayName("should throw exception when balance incorrect")
    void shouldThrowInvalidFundsExceptionWhenBalanceIncorrect() {
        Account account = AccountTestBuilder.anAccount()
                .withId(100L)
                .withPlayerId(100L)
                .withCurrentBalance(BigDecimal.ZERO)
                .build();
        BigDecimal updatedBalance = account.getCurrentBalance().add(new BigDecimal("-500"));

        doReturn(account)
                .when(accountRepository).save(account);
        Account savedAccount = accountService.save(account);
        assertThat(savedAccount).isNotNull().isEqualTo(account);
        assertThrows(InvalidFundsException.class, () -> accountService.updateBalance(account, updatedBalance));
    }

    @Test
    @DisplayName("should delete account")
    void shouldDeleteAccount() {
        Account account = AccountTestBuilder.anAccount()
                .withId(100L)
                .withPlayerId(100L)
                .withCurrentBalance(BigDecimal.ZERO)
                .build();

        doReturn(account)
                .when(accountRepository).save(account);
        doReturn(Optional.empty())
                .when(accountRepository).findById(account.getId());

        Account savedAccount = accountService.save(account);
        Optional<Account> actualResult = accountService.findById(account.getId());

        assertThat(savedAccount).isNotNull().isEqualTo(account);
        assertThat(actualResult).isEmpty();
    }
}
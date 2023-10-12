package io.ylab.walletservice.service.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.repository.impl.AccountRepositoryImpl;
import io.ylab.walletservice.core.service.impl.AccountServiceImpl;
import io.ylab.walletservice.exception.InvalidIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    AccountServiceImpl accountService;
    @Mock
    AccountRepositoryImpl accountRepository;

    static final Account account = buildTestAccount();

    @Test
    @DisplayName("should find existing account by id")
    void shouldFindAccountById() {
        Long id = account.getId();
        Optional<Account> expectedAccount = Optional.of(account);

        doReturn(expectedAccount)
                .when(accountRepository).findById(id);

        Optional<Account> actualResult = accountService.findById(id);

        assertThat(actualResult).isPresent().isEqualTo(expectedAccount);
    }

    @Test
    @DisplayName("should return empty optional")
    void shouldReturnEmptyOptionalWhenAccountNotFound() {
        Long id = account.getId();
        Optional<Account> expectedAccount = Optional.empty();

        doReturn(expectedAccount)
                .when(accountRepository).findById(id);

        Optional<Account> actualResult = accountService.findById(id);

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
        Long playerId = account.getPlayerId();
        Optional<Account> expectedAccount = Optional.of(account);

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

        doReturn(List.of(account))
                .when(accountRepository).findAll();

        List<Account> actualResult = accountService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactly(account);
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
        doReturn(account)
                .when(accountRepository).save(account);

        Account actualResult = accountService.save(account);

        assertThat(actualResult).isNotNull().isEqualTo(account);
    }

    private static Account buildTestAccount() {
        return Account.builder()
                .playerId(999L)
                .build();
    }
}
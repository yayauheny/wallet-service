package yayauheny.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import yayauheny.entity.Account;
import yayauheny.entity.Currency;
import yayauheny.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static yayauheny.repository.impl.AccountRepositoryImpl.accountsMap;


@DisplayName("AccountRepositoryImpl Tests")
public class AccountRepositoryImplTest {

    private final AccountRepository<Long, Account> accountRepository = AccountRepositoryImpl.getInstance();
    private final Account testAccount = createTestAccount();

    @BeforeEach
    void setUp() {
        accountsMap.clear();
    }

    @Test
    @DisplayName("shouldFindAccountById")
    void shouldFindAccountById() {
        accountRepository.save(testAccount);

        Optional<Account> foundAccount = accountRepository.findById(testAccount.getId());

        assertTrue(foundAccount.isPresent());
        assertEquals(testAccount, foundAccount.get());
    }

    @Test
    @DisplayName("shouldNotFindAccountById")
    void shouldNotFindAccountById() {
        Optional<Account> foundAccount = accountRepository.findById(999L);

        assertTrue(foundAccount.isEmpty());
    }

    @Test
    @DisplayName("shouldFindAccountByPlayerId")
    void shouldFindAccountByPlayerId() {
        accountRepository.save(testAccount);

        Optional<Account> foundAccount = accountRepository.findByPlayerId(testAccount.getPlayerId());

        assertTrue(foundAccount.isPresent());
        assertEquals(testAccount, foundAccount.get());
    }

    @Test
    @DisplayName("shouldNotFindAccountByPlayerId")
    void shouldNotFindAccountByPlayerId() {
        Optional<Account> foundAccount = accountRepository.findByPlayerId(999L);

        assertTrue(foundAccount.isEmpty());
    }

    @Test
    @DisplayName("shouldFindAllAccounts")
    void shouldFindAllAccounts() {
        accountRepository.save(testAccount);

        List<Account> allAccounts = accountRepository.findAll();

        assertFalse(allAccounts.isEmpty());
        assertTrue(allAccounts.contains(testAccount));
    }

    @Test
    @DisplayName("shouldSaveAccount")
    void shouldSaveAccount() {
        Account savedAccount = accountRepository.save(testAccount);

        assertNotNull(savedAccount);
        assertNotNull(savedAccount.getId());
        assertEquals(testAccount.getCurrentBalance(), savedAccount.getCurrentBalance());
        assertTrue(accountsMap.containsKey(savedAccount.getId()));
    }

    @Test
    @DisplayName("shouldUpdateAccount")
    void shouldUpdateAccount() {
        accountRepository.save(testAccount);

        Account updatedAccount = createTestAccount();
        updatedAccount.setId(testAccount.getId());
        accountRepository.update(updatedAccount);

        assertEquals(updatedAccount, accountsMap.get(testAccount.getId()));
    }

    @Test
    @DisplayName("shouldDeleteAccount")
    void shouldDeleteAccount() {
        accountRepository.save(testAccount);

        boolean deleted = accountRepository.delete(testAccount);

        assertTrue(deleted);
        assertTrue(accountsMap.isEmpty());
    }

    private Account createTestAccount() {
        return Account.builder()
                .currentBalance(BigDecimal.valueOf(1000))
                .currency(new Currency(BigDecimal.ONE, "USD"))
                .playerId(100L)
                .build();
    }
}

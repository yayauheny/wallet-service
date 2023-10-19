package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.exception.InvalidFundsException;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.exception.TransactionException;
import io.ylab.walletservice.testutil.PostgresTestcontainer;
import io.ylab.walletservice.testutil.TestObjectsUtil;
import io.ylab.walletservice.testutil.TransactionTestBuilder;
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
class TransactionServiceImplTest {

    static TransactionServiceImpl transactionService = new TransactionServiceImpl();
    static CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
    static PlayerServiceImpl playerService = new PlayerServiceImpl();
    static AccountServiceImpl accountService = new AccountServiceImpl();

    @BeforeAll
    static void startContainer() {
        PostgresTestcontainer.init();
        TestObjectsUtil.createObjects(currencyService, playerService, accountService);
        try {
            transactionService.save(TEST_TRANSACTION, TEST_ACCOUNT_HANNA);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void closeContainer() {
        PostgresTestcontainer.close();
    }

    @Test
    @DisplayName("should find existing transaction by id")
    void shouldFindTransactionById() throws DatabaseException {
        Optional<Transaction> expectedTransaction = Optional.of(TEST_TRANSACTION);
        Optional<Transaction> actualResult = transactionService.findById(TEST_TRANSACTION.getId());

        assertThat(actualResult).isPresent().isEqualTo(expectedTransaction);
    }

    @Test
    @DisplayName("should return an empty optional when transaction not found")
    void shouldReturnEmptyOptionalWhenTransactionNotFound() throws DatabaseException {
        Optional<Transaction> actualResult = transactionService.findById(999L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should throw an exception if id is null or negative")
    void shouldThrowInvalidIdExceptionIfIdIsInvalid() {
        Long emptyId = null;
        Long negativeId = -1L;

        assertThrows(InvalidIdException.class, () -> transactionService.findById(emptyId));
        assertThrows(InvalidIdException.class, () -> transactionService.findById(negativeId));
    }

    @Test
    @DisplayName("should save and return saved transaction")
    void shouldSaveAndReturnTransaction() throws DatabaseException {
        Transaction actualResult = transactionService.save(TEST_TRANSACTION_CREDIT, TEST_ACCOUNT_HANNA);

        assertThat(actualResult).isNotNull().isEqualTo(TEST_TRANSACTION_CREDIT);
    }

    @Test
    @DisplayName("should throw TransactionException when amount is bigger than current balance")
    void shouldThrowTransactionExceptionWhenAmountBiggerThanBalance() {
        Transaction transaction = TransactionTestBuilder.aTransaction()
                .withAmount(new BigDecimal("1200.00"))
                .withType(TransactionType.DEBIT)
                .withCreatedAt(LOCAL_DATE_TIME)
                .withCurrencyCode("EUR")
                .withParticipantAccount(TEST_ACCOUNT_HANNA)
                .build();

        assertThrows(TransactionException.class, () -> transactionService.processTransactionAndUpdateAccount(transaction, TEST_ACCOUNT_HANNA));
    }

    @Test
    @DisplayName("should throw InvalidFundsException when amount is negative")
    void shouldThrowInvalidFundsExceptionWhenAmountIsNegative() {
        Transaction transaction = TransactionTestBuilder.aTransaction()
                .withAmount(new BigDecimal("-12.00"))
                .withType(TransactionType.DEBIT)
                .withCreatedAt(LOCAL_DATE_TIME)
                .withCurrencyCode("EUR")
                .withParticipantAccount(TEST_ACCOUNT_HANNA)
                .build();

        assertThrows(InvalidFundsException.class, () -> transactionService.processTransactionAndUpdateAccount(transaction, TEST_ACCOUNT_ANDREW));
    }

    @Test
    @DisplayName("should throw TransactionException when currency not set")
    void shouldThrowTransactionExceptionWhenCurrencyNotSet() {
        Transaction transaction = TransactionTestBuilder.aTransaction()
                .withAmount(new BigDecimal("12.00"))
                .withType(TransactionType.DEBIT)
                .withCreatedAt(LOCAL_DATE_TIME)
                .withParticipantAccount(TEST_ACCOUNT_HANNA)
                .build();

        assertThrows(TransactionException.class, () -> transactionService.processTransactionAndUpdateAccount(transaction, TEST_ACCOUNT_ANDREW));
    }

    @Test
    @DisplayName("should return all transactions for specified account")
    void shouldReturnAllTransactionsByAccountId() throws DatabaseException {
        Long accountId = TEST_ACCOUNT_HANNA.getId();
        int expectedSize = 1;

        List<Transaction> actualResult = transactionService.findAllByAccountId(accountId);

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_TRANSACTION);
    }

    @Test
    @DisplayName("should delete transaction")
    void shouldDeleteTransaction() throws DatabaseException {
        transactionService.delete(TEST_TRANSACTION);
        Optional<Transaction> actualResult = transactionService.findById(TEST_TRANSACTION.getId());

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should return all transactions")
    void shouldReturnAllTransactions() throws DatabaseException {
        int expectedSize = 1;
        List<Transaction> actualResult = transactionService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_TRANSACTION_CREDIT);
    }
}
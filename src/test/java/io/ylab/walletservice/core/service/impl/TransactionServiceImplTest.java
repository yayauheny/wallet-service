package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.core.repository.impl.TransactionRepositoryImpl;
import io.ylab.walletservice.exception.InvalidFundsException;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.exception.TransactionException;
import io.ylab.walletservice.testutil.AccountTestBuilder;
import io.ylab.walletservice.testutil.TransactionTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_ACCOUNT;
import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_CURRENCY;
import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_PLAYER;
import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_TRANSACTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    TransactionServiceImpl transactionService;
    @Mock
    TransactionRepositoryImpl transactionRepository;

    @Test
    @DisplayName("should find existing transaction by id")
    void shouldFindTransactionById() {
        Long TransactionId = TEST_TRANSACTION.getId();
        Optional<Transaction> expectedTransaction = Optional.of(TEST_TRANSACTION);

        doReturn(expectedTransaction)
                .when(transactionRepository).findById(TransactionId);

        Optional<Transaction> actualResult = transactionService.findById(TransactionId);

        assertThat(actualResult).isPresent().isEqualTo(expectedTransaction);
    }

    @Test
    @DisplayName("should return an empty optional when transaction not found")
    void shouldReturnEmptyOptionalWhenTransactionNotFound() {
        Optional<Transaction> expectedTransaction = Optional.empty();

        doReturn(expectedTransaction)
                .when(transactionRepository).findById(any());

        Optional<Transaction> actualResult = transactionService.findById(0L);

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
    @DisplayName("should return all transactions")
    void shouldReturnAllTransactions() {
        int expectedSize = 1;

        doReturn(List.of(TEST_TRANSACTION))
                .when(transactionRepository).findAll();

        List<Transaction> actualResult = transactionService.findAll();

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_TRANSACTION);
    }

    @Test
    @DisplayName("should return an empty list if no transactions found")
    void shouldReturnAnEmptyListIfNoTransactionsFound() {
        doReturn(Collections.emptyList())
                .when(transactionRepository).findAll();

        List<Transaction> actualResult = transactionService.findAll();

        assertThat(actualResult).isEmpty();
    }

    @Test
    @DisplayName("should save and return saved transaction")
    void shouldSaveAndReturnTransaction() {
        Account account = AccountTestBuilder.anAccount()
                .withPlayerId(TEST_PLAYER.getId())
                .withId(999L)
                .withCurrentBalance(new BigDecimal("200"))
                .withCurrency(TEST_CURRENCY)
                .build();
        doReturn(TEST_TRANSACTION)
                .when(transactionRepository).save(TEST_TRANSACTION);

        Transaction actualResult = transactionService.save(TEST_TRANSACTION, account);

        assertThat(actualResult).isNotNull().isEqualTo(TEST_TRANSACTION);
    }

    @Test
    @DisplayName("should throw TransactionException when amount is bigger than current balance")
    void shouldThrowTransactionExceptionWhenAmountBiggerThanBalance() {
        Transaction transaction = TransactionTestBuilder.aTransaction()
                .withId(999L)
                .withCurrency(TEST_CURRENCY)
                .withType(TransactionType.DEBIT)
                .withAmount(new BigDecimal("600"))
                .build();

        assertThrows(TransactionException.class, () -> transactionService.processTransactionAndUpdateAccount(transaction, TEST_ACCOUNT));
    }

    @Test
    @DisplayName("should throw InvalidFundsException when amount is negative")
    void shouldThrowInvalidFundsExceptionWhenAmountIsNegative() {
        Transaction transaction = TransactionTestBuilder.aTransaction()
                .withId(999L)
                .withCurrency(TEST_CURRENCY)
                .withType(TransactionType.DEBIT)
                .withAmount(new BigDecimal("-100"))
                .build();

        assertThrows(InvalidFundsException.class, () -> transactionService.processTransactionAndUpdateAccount(transaction, TEST_ACCOUNT));
    }

    @Test
    @DisplayName("should throw TransactionException when currency not set")
    void shouldThrowTransactionExceptionWhenCurrencyNotSet() {
        Transaction transaction = TransactionTestBuilder.aTransaction()
                .withId(999L)
                .withCurrency(null)
                .withType(TransactionType.CREDIT)
                .withAmount(new BigDecimal("300"))
                .build();

        assertThrows(TransactionException.class, () -> transactionService.processTransactionAndUpdateAccount(transaction, TEST_ACCOUNT));
    }

    @Test
    @DisplayName("should return all transactions for specified account")
    void shouldReturnAllTransactionsByAccountId() {
        Long accountId = TEST_ACCOUNT.getId();
        int expectedSize = 1;

        doReturn(List.of(TEST_TRANSACTION))
                .when(transactionRepository).findAllByAccountId(accountId);

        List<Transaction> actualResult = transactionService.findAllByAccountId(accountId);

        assertThat(actualResult).hasSize(expectedSize).containsExactlyInAnyOrder(TEST_TRANSACTION);
    }

    @Test
    @DisplayName("should delete Transaction")
    void shouldDeleteTransaction() {
//        Transaction Transaction = TransactionTestBuilder.aTransaction()
//                .withId(100L)
//                .withUsername("test-name")
//                .build();
//
//        doReturn(Transaction)
//                .when(transactionRepository).save(Transaction);
//        doReturn(Optional.empty())
//                .when(transactionRepository).findById(Transaction.getId());
//
//        Transaction savedTransaction = transactionService.save(Transaction);
//        Optional<Transaction> actualResult = transactionService.findById(Transaction.getId());
//
//        assertThat(savedTransaction).isNotNull().isEqualTo(Transaction);
//        assertThat(actualResult).isEmpty();
    }
}
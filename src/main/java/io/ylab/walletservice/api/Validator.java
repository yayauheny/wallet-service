package io.ylab.walletservice.api;

import io.ylab.walletservice.core.dto.ReceiptDto;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.exception.IncorrectPeriodException;
import io.ylab.walletservice.exception.InvalidFundsException;
import io.ylab.walletservice.exception.InvalidIdException;
import io.ylab.walletservice.exception.ReceiptBuildingException;
import io.ylab.walletservice.exception.TransactionException;
import lombok.experimental.UtilityClass;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * The {@link Validator} utility class provides methods for validating various entities and conditions
 * to ensure the correctness of data and prevent exceptions during the application's execution.
 */
@UtilityClass
public class Validator {

    /**
     * Validates the given ID to ensure it is not null and non-negative.
     *
     * @param id  The ID to validate.
     * @param <T> The type of the ID (Number).
     * @throws InvalidIdException If the ID is null or negative.
     */
    public <T extends Number> void validateId(T id) {
        if (id == null || id.intValue() < 0) {
            throw new InvalidIdException("Cannot find by id = " + id);
        }
    }

    /**
     * Validates the funds in a transaction, ensuring that the amount is non-negative and
     * the debit transaction does not exceed the participant's balance.
     *
     * @param transaction The transaction to validate.
     * @throws TransactionException If the transaction amount is negative or exceeds the balance for debit transactions.
     */
    public void validateTransactionFunds(Transaction transaction) {
        BigDecimal participantBalance = transaction.getParticipantAccount().getCurrentBalance();
        BigDecimal transactionAmount = transaction.getAmount();
        TransactionType transactionType = transaction.getType();

        validateAmount(transactionAmount);
        if (transactionType.equals(TransactionType.DEBIT) && participantBalance.compareTo(transactionAmount) < 0) {
            throw new TransactionException("Cannot perform transfer, incorrect amount");
        }
    }

    /**
     * Validates that the amount in a transaction is non-negative.
     *
     * @param amount The amount to validate.
     * @throws InvalidFundsException If the amount is negative.
     */
    public void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidFundsException("Cannot perform transaction, amount: " + amount + " is negative");
        }
    }

    /**
     * Validates the period for generating a transaction receipt, ensuring that the start date is before the end date.
     *
     * @param from The start date.
     * @param to   The end date.
     * @throws IncorrectPeriodException If the period is incorrectly specified.
     */
    public void validateTransactionsPeriod(LocalDateTime from, LocalDateTime to) {
        String exceptionMessage = "Incorrect period has been passed";
        if (from == null
            || to == null
            || from.isAfter(to)
            || to.isBefore(from)
        ) {
            throw new IncorrectPeriodException(exceptionMessage);
        }
    }

    /**
     * Validates the receiver account in a transaction to ensure it is not null.
     *
     * @param receiver The receiver account.
     * @throws TransactionException If the receiver account is null.
     */
    public void validateTransactionReceiver(Account receiver) {
        if (receiver == null) {
            throw new TransactionException("Exception while processing transaction. Receiver is empty. Try again");
        }
    }

    /**
     * Validates the currency in a transaction to ensure it is not null.
     *
     * @param currency The currency.
     * @throws TransactionException If the currency is null.
     */
    public void validateTransactionCurrency(Currency currency) {
        if (currency == null) {
            throw new TransactionException("Exception while processing transaction. Currency is empty. Try again");
        }
    }

    /**
     * Validates a transaction by checking its funds and currency.
     *
     * @param transaction The transaction to validate.
     */
    public void validateTransaction(Transaction transaction) {
        validateTransactionFunds(transaction);
        validateTransactionCurrency(transaction.getCurrency());
    }

    /**
     * Validates a receipt for basic information, ensuring that key components are not null.
     *
     * @param receiptDto The receipt to validate.
     * @throws ReceiptBuildingException If the receipt is missing essential information.
     */
    public void validateReceiptForCheck(ReceiptDto receiptDto) {
        if (receiptDto.account() == null || receiptDto.player() == null || receiptDto.from() == null || receiptDto.to() == null) {
            throw new ReceiptBuildingException();
        }
    }

    /**
     * Validates a receipt for money statement, ensuring it has essential information and a valid period.
     *
     * @param receiptDto The receipt to validate.
     * @throws ReceiptBuildingException If the receipt is missing essential information or has an incorrect period.
     */
    public void validateReceiptForMoneyStatement(ReceiptDto receiptDto) {
        validateReceiptForCheck(receiptDto);
        validateTransactionsPeriod(receiptDto.from(), receiptDto.to());
    }
}


package io.ylab.walletservice.core.service;


import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.dto.account.AccountCreateDto;
import io.ylab.walletservice.core.dto.account.AccountUpdateDto;
import io.ylab.walletservice.core.dto.transaction.TransactionRequest;
import io.ylab.walletservice.exception.DatabaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * The service interface for managing transaction entities.
 *
 * @param <K> The type of the identifier used for transaction entities.
 */
public interface TransactionService<K> {

    /**
     * Retrieves a transaction entity by its unique identifier.
     *
     * @param id The unique identifier of the transaction entity to find.
     * @return An {@link Optional} containing the found transaction entity, or an empty {@link Optional} if not found.
     */
    Optional<Transaction> findById(K id) throws DatabaseException;

    /**
     * Retrieves a list of transactions within a specified period for a given account.
     *
     * @param from       The start date of the period.
     * @param to         The end date of the period.
     * @param accountId  The unique identifier of the account.
     * @return A list of transactions within the specified period for the given account.
     */
    List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to, Long accountId) throws DatabaseException;

    /**
     * Retrieves a list of all transaction entities.
     *
     * @return A list of all transaction entities.
     */
    List<Transaction> findAll() throws DatabaseException;

    /**
     * Retrieves a list of all transactions associated with a specific account.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of all transactions associated with the specified account.
     */
    List<Transaction> findAllByAccountId(K accountId) throws DatabaseException;

    /**
     * Saves a new transaction entity and associates it with the provided account.
     *
     * @param transaction The transaction entity to save.
     * @param account     The account to associate the transaction with.
     * @return The saved transaction entity.
     */
    Transaction save(Transaction transaction, Account account) throws DatabaseException;

    /**
     * Processes a transaction and updates the associated account.
     *
     * @param transactionDto The TransactionRequest instance to process.
     * @param accountDto     The AccountRequest instance to update.
     */
    void processTransactionAndUpdateAccount(TransactionRequest transactionDto, AccountUpdateDto accountDto) throws DatabaseException;

    /**
     * Deletes an existing transaction entity.
     *
     * @param id The transaction id to delete.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
    boolean delete(Long id) throws DatabaseException;
}


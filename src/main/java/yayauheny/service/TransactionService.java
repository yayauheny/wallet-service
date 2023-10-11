package yayauheny.service;


import yayauheny.entity.Account;
import yayauheny.entity.Transaction;

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
    Optional<Transaction> findById(K id);

    /**
     * Retrieves a list of transactions within a specified period for a given account.
     *
     * @param from       The start date of the period.
     * @param to         The end date of the period.
     * @param accountId  The unique identifier of the account.
     * @return A list of transactions within the specified period for the given account.
     */
    List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to, Long accountId);

    /**
     * Retrieves a list of all transaction entities.
     *
     * @return A list of all transaction entities.
     */
    List<Transaction> findAll();

    /**
     * Retrieves a list of all transactions associated with a specific account.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of all transactions associated with the specified account.
     */
    List<Transaction> findAllByAccountId(K accountId);

    /**
     * Saves a new transaction entity and associates it with the provided account.
     *
     * @param transaction The transaction entity to save.
     * @param account     The account to associate the transaction with.
     * @return The saved transaction entity.
     */
    Transaction save(Transaction transaction, Account account);

    /**
     * Processes a transaction and updates the associated account.
     *
     * @param transaction The transaction to process.
     * @param account     The account to update.
     */
    void processTransactionAndUpdateAccount(Transaction transaction, Account account);

    /**
     * Deletes an existing transaction entity.
     *
     * @param transaction The transaction entity to delete.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
    boolean delete(Transaction transaction);
}


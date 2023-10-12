package io.ylab.walletservice.core.repository;

import io.ylab.walletservice.core.domain.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The {@code TransactionRepository} interface defines methods for accessing and
 * manipulating transaction entities in the repository.
 *
 * @param <K> The type of the identifier for the transaction.
 */
public interface TransactionRepository<K> {

    /**
     * Finds a transaction by its identifier.
     *
     * @param id The identifier of the transaction to find.
     * @return An optional containing the transaction entity if found, otherwise empty.
     */
    Optional<Transaction> findById(K id);

    /**
     * Retrieves a list of transactions within a specified period for a given account.
     *
     * @param from       The start date and time of the period.
     * @param to         The end date and time of the period.
     * @param accountId  The identifier of the account for which transactions are retrieved.
     * @return A list of transaction entities.
     */
    List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to, Long accountId);

    /**
     * Retrieves a list of transactions for a given account.
     *
     * @param accountId The identifier of the account for which transactions are retrieved.
     * @return A list of transaction entities.
     */
    List<Transaction> findByAccountId(Long accountId);

    /**
     * Retrieves a list of all transactions in the repository.
     *
     * @return A list of transaction entities.
     */
    List<Transaction> findAll();

    /**
     * Saves a new transaction entity.
     *
     * @param transaction The transaction entity to be saved.
     * @return The saved transaction entity.
     */
    Transaction save(Transaction transaction);

    /**
     * Deletes an existing transaction entity.
     *
     * @param transaction The transaction entity to be deleted.
     * @return {@code true} if the deletion is successful, otherwise {@code false}.
     */
    boolean delete(Transaction transaction);
}


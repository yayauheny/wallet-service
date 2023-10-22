package io.ylab.walletservice.core.repository;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * The {@link AccountRepository} interface defines methods for accessing and
 * manipulating account entities in the repository.
 *
 * @param <K> The type of the identifier for the account.
 */
public interface AccountRepository<K> {

    /**
     * Finds an account by its identifier.
     *
     * @param id The identifier of the account to find.
     * @return An optional containing the account entity if found, otherwise empty.
     * @throws DatabaseException if a database access error occurs or
     *                           {@link Account} entity cannot be found.
     */
    Optional<Account> findById(K id) throws DatabaseException;

    /**
     * Finds an account by the player's identifier.
     *
     * @param playerId The identifier of the player associated with the account.
     * @return An optional containing the account entity if found, otherwise empty.
     * @throws DatabaseException if a database access error occurs or
     *                           {@link Account} entity cannot be found.
     */
    Optional<Account> findByPlayerId(K playerId) throws DatabaseException;

    /**
     * Retrieves a list of all accounts in the repository.
     *
     * @return A list of account entities.
     * * @throws DatabaseException if a database access error occurs or
     * *                           {@link Account} entities cannot be retrieved.
     */
    List<Account> findAll() throws DatabaseException;

    /**
     * Retrieves a list of all transactions for account.
     *
     * @return A list of transactions entities.
     * * @throws DatabaseException if a database access error occurs or
     * *                           {@link Transaction} entities cannot be retrieved.
     */
    List<Transaction> findAllTransactions(Long accountId) throws DatabaseException;

    /**
     * Saves a new account entity or updates an existing one.
     *
     * @param account The account entity to be saved or updated.
     * @return The saved or updated account entity.
     * @throws DatabaseException if a database access error occurs or
     *                           {@link Account} entity cannot be saved.
     */
    Account save(Account account) throws DatabaseException;

    /**
     * Updates an existing account entity.
     *
     * @param account The account entity to be updated.
     */
    void update(Account account) throws DatabaseException;

    /**
     * Deletes an existing account entity.
     *
     * @param account The account entity to be deleted.
     * @return {@code true} if the deletion is successful, otherwise {@code false}.
     * @throws DatabaseException if a database access error occurs or
     *                           {@link Account} entity cannot be deleted.
     */
    boolean delete(Account account) throws DatabaseException;
}


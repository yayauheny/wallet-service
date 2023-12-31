package io.ylab.walletservice.core.service;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.dto.account.AccountCreateDto;
import io.ylab.walletservice.core.dto.account.AccountResponse;
import io.ylab.walletservice.core.dto.account.AccountUpdateDto;
import io.ylab.walletservice.exception.DatabaseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


/**
 * The service interface for managing {@link Account} entities.
 *
 * @param <K> The type of the identifier used for accounts.
 */
public interface AccountService<K> {

    /**
     * Retrieves an {@link Account} by its unique identifier.
     *
     * @param id The unique identifier of the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws IllegalArgumentException if the provided {@code id} is null or less than or equal to zero.
     */
    Optional<Account> findById(K id) throws DatabaseException;

    /**
     * Retrieves an {@link Account} by the player's unique identifier.
     *
     * @param playerId The unique identifier of the player associated with the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws IllegalArgumentException if the provided {@code playerId} is null or less than or equal to zero.
     */
    Optional<Account> findByPlayerId(K playerId) throws DatabaseException;

    /**
     * Retrieves a list of all {@link Account} entities.
     *
     * @return A list of all accounts.
     */
    List<Account> findAll() throws DatabaseException;

    /**
     * Retrieves a list of all transactions for account.
     *
     * @return A list of transactions entities.
     * * @throws DatabaseException if a database access error occurs or
     * *                           {@link Transaction} entities cannot be retrieved.
     */
    List<Transaction> findAllTransactions(Account account) throws DatabaseException;

    /**
     * Save a new {@link Account} .
     *
     * @param account The account to save.
     * @return The saved account.
     */
    Account save(AccountCreateDto account) throws DatabaseException;

    /**
     * Updates an existing {@link Account}.
     *
     * @param account The account to update.
     */
    void update(AccountUpdateDto account) throws DatabaseException;

    /**
     * Updates the balance of an existing {@link Account}.
     *
     * @param account          The account whose balance will be updated.
     * @param updatedBalance   The new balance value.
     */
    void updateBalance(AccountUpdateDto account, BigDecimal updatedBalance) throws DatabaseException;

    /**
     * Deletes an existing {@link Account}.
     *
     * @param id The account id to delete.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
    boolean delete(Long id) throws DatabaseException;
}


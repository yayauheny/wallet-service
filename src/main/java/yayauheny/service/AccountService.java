package yayauheny.service;

import yayauheny.entity.Account;

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
    Optional<Account> findById(K id);

    /**
     * Retrieves an {@link Account} by the player's unique identifier.
     *
     * @param playerId The unique identifier of the player associated with the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws IllegalArgumentException if the provided {@code playerId} is null or less than or equal to zero.
     */
    Optional<Account> findByPlayerId(K playerId);

    /**
     * Retrieves a list of all {@link Account} entities.
     *
     * @return A list of all accounts.
     */
    List<Account> findAll();

    /**
     * Save a new {@link Account} .
     *
     * @param account The account to save.
     * @return The saved account.
     */
    Account save(Account account);

    /**
     * Updates an existing {@link Account}.
     *
     * @param account The account to update.
     */
    void update(Account account);

    /**
     * Updates the balance of an existing {@link Account}.
     *
     * @param account          The account whose balance will be updated.
     * @param updatedBalance   The new balance value.
     */
    void updateBalance(Account account, BigDecimal updatedBalance);

    /**
     * Deletes an existing {@link Account}.
     *
     * @param account The account to delete.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
    boolean delete(Account account);
}


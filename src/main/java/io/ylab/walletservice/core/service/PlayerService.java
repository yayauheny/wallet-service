package io.ylab.walletservice.core.service;

import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.dto.player.PlayerCreateDto;
import io.ylab.walletservice.core.dto.player.PlayerUpdateDto;
import io.ylab.walletservice.core.dto.player.PlayerResponse;
import io.ylab.walletservice.core.dto.transaction.TransactionResponse;
import io.ylab.walletservice.exception.DatabaseException;

import java.util.List;
import java.util.Optional;


/**
 * The service interface for managing player entities.
 *
 * @param <K> The type of the identifier used for player entities.
 */
public interface PlayerService<K> {

    /**
     * Retrieves a player entity by its unique identifier.
     *
     * @param id The unique identifier of the player entity to find.
     * @return An {@link Optional} containing the found player entity, or an empty {@link Optional} if not found.
     */
    Optional<Player> findById(K id) throws DatabaseException;

    /**
     * Retrieves a player entity by its username.
     *
     * @param username The username of the player entity to find.
     * @return An {@link Optional} containing the found player entity, or an empty {@link Optional} if not found.
     */
    Optional<Player> findByUsername(String username) throws DatabaseException;

    /**
     * Retrieves a list of all player entities.
     *
     * @return A list of all player entities.
     */
    List<Player> findAll() throws DatabaseException;

    /**
     * Retrieves a list of transactions associated with a player.
     *
     * @param id The unique identifier of the player entity.
     * @return A list of transactions associated with the player.
     * @throws DatabaseException If the player entity is not found.
     */
    List<Transaction> getTransactions(K id) throws DatabaseException;

    /**
     * Saves a new player entity if it does not exist, otherwise returns the existing one.
     *
     * @param playerDto The {@link Player} entity to save.
     * @return The saved or existing player entity.
     */
    Player save(PlayerCreateDto playerDto) throws DatabaseException;

    /**
     * Updates an existing player entity. Note: In this context, updating means modifying the existing entity,
     * and this operation should be performed outside the scope of this method.
     *
     * @param playerDto The player entity to update.
     */
    void update(PlayerUpdateDto playerDto) throws DatabaseException;

    /**
     * Deletes an existing player entity.
     *
     * @param id The player id to delete.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
    boolean delete(Long id) throws DatabaseException;
    //TODO ADD DOC
    boolean verifyPassword(String password, String username);
}


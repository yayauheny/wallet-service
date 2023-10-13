package io.ylab.walletservice.core.service;

import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.exception.NotFoundException;
import io.ylab.walletservice.core.domain.Player;

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
    Optional<Player> findById(K id);

    /**
     * Retrieves a player entity by its username.
     *
     * @param username The username of the player entity to find.
     * @return An {@link Optional} containing the found player entity, or an empty {@link Optional} if not found.
     */
    Optional<Player> findByUsername(String username);

    /**
     * Retrieves a list of all player entities.
     *
     * @return A list of all player entities.
     */
    List<Player> findAll();

    /**
     * Retrieves a list of transactions associated with a player.
     *
     * @param id The unique identifier of the player entity.
     * @return A list of transactions associated with the player.
     * @throws io.ylab.walletservice.exception.NotFoundException If the player entity is not found.
     */
    List<Transaction> getTransactions(K id) throws NotFoundException;

    /**
     * Saves a new player entity if it does not exist, otherwise returns the existing one.
     *
     * @param player The {@link Player} entity to save.
     * @return The saved or existing player entity.
     */
    Player save(Player player);

    /**
     * Updates an existing player entity. Note: In this context, updating means modifying the existing entity,
     * and this operation should be performed outside the scope of this method.
     *
     * @param player The player entity to update.
     */
    void update(Player player);

    /**
     * Deletes an existing player entity.
     *
     * @param player The player entity to delete.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
    boolean delete(Player player);
}


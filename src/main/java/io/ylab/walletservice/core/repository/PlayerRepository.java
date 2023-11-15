package io.ylab.walletservice.core.repository;

import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * The {@link PlayerRepository} interface defines methods for accessing and
 * manipulating player entities in the repository.
 *
 * @param <K> The type of the identifier for the player.
 */
public interface PlayerRepository<K> {

    /**
     * Finds a player by its identifier.
     *
     * @param id The identifier of the player to find.
     * @return An optional containing the player entity if found, otherwise empty.
     */
    Optional<Player> findById(K id) throws DatabaseException;

    /**
     * Finds a player by its username.
     *
     * @param username The username of the player to find.
     * @return An optional containing the player entity if found, otherwise empty.
     */
    Optional<Player> findByUsername(String username) throws DatabaseException;

    /**
     * Retrieves a list of all players in the repository.
     *
     * @return A list of player entities.
     */
    List<Player> findAll() throws DatabaseException;

    /**
     * Saves a new player entity.
     *
     * @param player The player entity to be saved.
     * @return player - {@link Player} entity
     */
    Player save(Player player) throws DatabaseException;

    /**
     * Updates an existing player entity.
     *
     * @param player The player entity to be updated.
     */
    void update(Player player) throws DatabaseException;

    /**
     * Deletes an existing player entity.
     *
     * @param id The player id to be deleted.
     * @return {@code true} if the deletion is successful, otherwise {@code false}.
     */
    boolean delete(Long id) throws DatabaseException;
}


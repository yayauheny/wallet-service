package io.ylab.walletservice.core.repository;

import io.ylab.walletservice.core.domain.Player;

import java.util.List;
import java.util.Optional;

/**
 * The {@link PlayerRepository} interface defines methods for accessing and
 * manipulating player entities in the repository.
 *
 * @param <K> The type of the identifier for the player.
 * @param <E> The type of the player entity.
 */
public interface PlayerRepository<K, E> {

    /**
     * Finds a player by its identifier.
     *
     * @param id The identifier of the player to find.
     * @return An optional containing the player entity if found, otherwise empty.
     */
    Optional<E> findById(K id);

    /**
     * Finds a player by its username.
     *
     * @param username The username of the player to find.
     * @return An optional containing the player entity if found, otherwise empty.
     */
    Optional<Player> findByUsername(String username);

    /**
     * Retrieves a list of all players in the repository.
     *
     * @return A list of player entities.
     */
    List<E> findAll();

    /**
     * Saves a new player entity.
     *
     * @param player The player entity to be saved.
     * @return player - {@link Player} entity
     */
    Player save(E player);

    /**
     * Updates an existing player entity.
     *
     * @param player The player entity to be updated.
     */
    void update(E player);

    /**
     * Deletes an existing player entity.
     *
     * @param player The player entity to be deleted.
     * @return {@code true} if the deletion is successful, otherwise {@code false}.
     */
    boolean delete(E player);
}


package yayauheny.repository;

import java.util.List;
import java.util.Optional;

/**
 * The {@link AccountRepository} interface defines methods for accessing and
 * manipulating account entities in the repository.
 *
 * @param <K> The type of the identifier for the account.
 * @param <E> The type of the account entity.
 */
public interface AccountRepository<K, E> {

    /**
     * Finds an account by its identifier.
     *
     * @param id The identifier of the account to find.
     * @return An optional containing the account entity if found, otherwise empty.
     */
    Optional<E> findById(K id);

    /**
     * Finds an account by the player's identifier.
     *
     * @param playerId The identifier of the player associated with the account.
     * @return An optional containing the account entity if found, otherwise empty.
     */
    Optional<E> findByPlayerId(K playerId);

    /**
     * Retrieves a list of all accounts in the repository.
     *
     * @return A list of account entities.
     */
    List<E> findAll();

    /**
     * Saves a new account entity or updates an existing one.
     *
     * @param account The account entity to be saved or updated.
     * @return The saved or updated account entity.
     */
    E save(E account);

    /**
     * Updates an existing account entity.
     *
     * @param account The account entity to be updated.
     */
    void update(E account);

    /**
     * Deletes an existing account entity.
     *
     * @param account The account entity to be deleted.
     * @return {@code true} if the deletion is successful, otherwise {@code false}.
     */
    boolean delete(E account);
}


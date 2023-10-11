package yayauheny.repository;

import java.util.List;
import java.util.Optional;

/**
 * The {@link CurrencyRepository} interface defines methods for accessing and
 * manipulating currency entities in the repository.
 *
 * @param <K> The type of the identifier for the currency.
 * @param <E> The type of the currency entity.
 */
public interface CurrencyRepository<K, E> {

    /**
     * Finds a currency by its identifier.
     *
     * @param id The identifier of the currency to find.
     * @return An optional containing the currency entity if found, otherwise empty.
     */
    Optional<E> findById(K id);

    /**
     * Finds a currency by its code.
     *
     * @param code The code of the currency to find.
     * @return An optional containing the currency entity if found, otherwise empty.
     */
    Optional<E> findByCode(String code);

    /**
     * Retrieves a list of all currencies in the repository.
     *
     * @return A list of currency entities.
     */
    List<E> findAll();

    /**
     * Saves a new currency entity or updates an existing one.
     *
     * @param currency The currency entity to be saved or updated.
     * @return The saved or updated currency entity.
     */
    E save(E currency);

    /**
     * Updates an existing currency entity.
     *
     * @param currency The currency entity to be updated.
     */
    void update(E currency);

    /**
     * Deletes an existing currency entity.
     *
     * @param currency The currency entity to be deleted.
     * @return {@code true} if the deletion is successful, otherwise {@code false}.
     */
    boolean delete(E currency);
}


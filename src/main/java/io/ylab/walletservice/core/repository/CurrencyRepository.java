package io.ylab.walletservice.core.repository;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

/**
 * The {@link CurrencyRepository} interface defines methods for accessing and
 * manipulating currency entities in the repository.
 *
 * @param <K> The type of the identifier for the currency.
 */
public interface CurrencyRepository<K> {

    /**
     * Finds a currency by its identifier.
     *
     * @param id The identifier of the currency to find.
     * @return An optional containing the currency entity if found, otherwise empty.
     */
    Optional<Currency> findById(K id) throws DatabaseException;

    /**
     * Finds a currency by its code.
     *
     * @param code The code of the currency to find.
     * @return An optional containing the currency entity if found, otherwise empty.
     */
    Optional<Currency> findByCode(String code) throws DatabaseException;

    /**
     * Retrieves a list of all currencies in the repository.
     *
     * @return A list of currency entities.
     */
    List<Currency> findAll() throws DatabaseException;

    /**
     * Saves a new currency entity or updates an existing one.
     *
     * @param currency The currency entity to be saved or updated.
     * @return The saved or updated currency entity.
     */
    Currency save(Currency currency) throws DatabaseException;

    /**
     * Updates an existing currency entity.
     *
     * @param currency The currency entity to be updated.
     */
    void update(Currency currency) throws DatabaseException;

    /**
     * Deletes an existing currency entity.
     *
     * @param currency The currency entity to be deleted.
     * @return {@code true} if the deletion is successful, otherwise {@code false}.
     */
    boolean delete(Currency currency) throws DatabaseException;
}


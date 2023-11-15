package io.ylab.walletservice.core.service;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.exception.DatabaseException;

import java.util.List;
import java.util.Optional;


/**
 * The service interface for managing entities related to currency.
 *
 * @param <K> The type of the identifier used for currency entities.
 */
public interface CurrencyService<K> {

    /**
     * Retrieves a currency entity by its unique identifier.
     *
     * @param id The unique identifier of the currency entity to find.
     * @return An {@link Optional} containing the found currency entity, or an empty {@link Optional} if not found.
     */
    Optional<Currency> findById(K id) throws DatabaseException;

    /**
     * Retrieves a currency entity by its code.
     *
     * @param code The code of the currency entity to find.
     * @return An {@link Optional} containing the found currency entity, or an empty {@link Optional} if not found.
     */
    Optional<Currency> findByCode(String code) throws DatabaseException;

    /**
     * Retrieves a list of all currency entities.
     *
     * @return A list of all currency entities.
     */
    List<Currency> findAll() throws DatabaseException;

    /**
     * Saves a new currency entity if it does not exist, otherwise returns the existing one.
     *
     * @param currency The currency entity to save.
     * @return The saved or existing currency entity.
     */
    Currency save(Currency currency) throws DatabaseException;

    /**
     * Updates an existing currency entity. Note: In this context, updating means modifying the existing entity,
     * and this operation should be performed outside the scope of this method.
     *
     * @param currency The currency entity to update.
     */
    void update(Currency currency) throws DatabaseException;

    /**
     * Deletes an existing currency entity.
     *
     * @param currency The currency entity to delete.
     * @return {@code true} if the deletion was successful, {@code false} otherwise.
     */
    boolean delete(Currency currency) throws DatabaseException;
}


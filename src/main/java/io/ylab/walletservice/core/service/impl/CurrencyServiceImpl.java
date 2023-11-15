package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.api.Validator;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.repository.impl.CurrencyRepositoryImpl;
import io.ylab.walletservice.core.service.CurrencyService;
import io.ylab.walletservice.exception.DatabaseException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;


/**
 * The implementation of the {@link io.ylab.walletservice.core.service.CurrencyService} interface.
 * Provides methods for interacting with {@link io.ylab.walletservice.core.domain.Currency} entities.
 */
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService<Long> {

    private final CurrencyRepositoryImpl currencyRepository;

    public CurrencyServiceImpl() {
        this.currencyRepository = CurrencyRepositoryImpl.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findById(Long id) throws DatabaseException {
        Validator.validateId(id);
        return currencyRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findByCode(String code) throws DatabaseException {
        return currencyRepository.findByCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Currency> findAll() throws DatabaseException {
        return currencyRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency save(Currency currency) throws DatabaseException {
        return currencyRepository.save(currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Currency currency) throws DatabaseException {
        currencyRepository.update(currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Currency currency) throws DatabaseException {
        return currencyRepository.delete(currency);
    }
}

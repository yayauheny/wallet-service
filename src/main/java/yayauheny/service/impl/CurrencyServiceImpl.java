package yayauheny.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import yayauheny.entity.Currency;
import yayauheny.repository.impl.CurrencyRepositoryImpl;
import yayauheny.service.CurrencyService;
import yayauheny.utils.Validator;

import java.util.List;
import java.util.Optional;

/**
 * The implementation of the {@link yayauheny.service.CurrencyService} interface.
 * Provides methods for interacting with {@link yayauheny.entity.Currency} entities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyServiceImpl implements CurrencyService<Long, Currency> {
    /**
     * The {@link yayauheny.repository.impl.AccountRepositoryImpl} used for database operations.
     */
    private final CurrencyRepositoryImpl currencyRepository = CurrencyRepositoryImpl.getInstance();

    /**
     * The singleton instance of the {@link CurrencyServiceImpl} class.
     */
    private static final CurrencyServiceImpl INSTANCE = new CurrencyServiceImpl();

    /**
     * Returns the singleton instance of the {@link CurrencyServiceImpl} class.
     *
     * @return The singleton instance.
     */
    public static CurrencyServiceImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findById(Long id) {
        Validator.validateId(id);

        return currencyRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Currency currency) {
        currencyRepository.update(currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Currency currency) {
        return currencyRepository.delete(currency);
    }
}

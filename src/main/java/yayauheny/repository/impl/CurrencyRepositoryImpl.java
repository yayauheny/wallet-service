package yayauheny.repository.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import yayauheny.entity.Currency;
import yayauheny.repository.CurrencyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link CurrencyRepositoryImpl} class implements the {@code CurrencyRepository} interface
 * for accessing and manipulating currency entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyRepositoryImpl implements CurrencyRepository<Long, Currency> {

    private static final CurrencyRepositoryImpl INSTANCE = new CurrencyRepositoryImpl();
    private static final int COLLECTION_DEFAULT_CAPACITY = 10;
    private final Map<Long, Currency> currenciesMap = new HashMap<>(COLLECTION_DEFAULT_CAPACITY);

    /**
     * Gets the singleton instance of the {@code CurrencyRepositoryImpl} class.
     *
     * @return The singleton instance.
     */
    public static CurrencyRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findById(Long id) {
        return Optional.ofNullable(currenciesMap.getOrDefault(id, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Currency> findByCode(String code) {
        return currenciesMap.values().stream()
                .filter(c -> c.getCode().equals(code))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Currency> findAll() {
        return List.copyOf(currenciesMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency save(Currency currency) {
        return currenciesMap.putIfAbsent(currency.getId(), currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Currency currency) {
        currenciesMap.replace(currency.getId(), currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Currency currency) {
        return currenciesMap.remove(currency.getId(), currency);
    }
}


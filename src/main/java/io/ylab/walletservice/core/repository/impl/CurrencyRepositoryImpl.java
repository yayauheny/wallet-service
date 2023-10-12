package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.repository.CurrencyRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The {@link CurrencyRepositoryImpl} class implements the {@code CurrencyRepository} interface
 * for accessing and manipulating currency entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyRepositoryImpl implements CurrencyRepository<Long, Currency> {

    private static final int COLLECTION_DEFAULT_CAPACITY = 10;
    public static final Map<Long, Currency> currenciesMap = new HashMap<>(COLLECTION_DEFAULT_CAPACITY);
    private static final CurrencyRepositoryImpl INSTANCE = new CurrencyRepositoryImpl();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

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
        if (currency.getId() == null) {
            idCounter.incrementAndGet();
            currency.setId(idCounter.longValue());
        }
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


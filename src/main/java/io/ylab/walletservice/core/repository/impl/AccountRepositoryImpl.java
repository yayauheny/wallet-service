package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link AccountRepositoryImpl} class implements the {@code AccountRepository} interface
 * for accessing and manipulating account entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRepositoryImpl implements AccountRepository<Long, Account> {

    private static final int COLLECTION_DEFAULT_CAPACITY = 10;
    private static final Map<Long, Account> ACCOUNTS_MAP = new HashMap<>(COLLECTION_DEFAULT_CAPACITY);
    private static final AccountRepositoryImpl INSTANCE = new AccountRepositoryImpl();
    private static AtomicInteger idCounter = new AtomicInteger(0);

    public static AccountRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(ACCOUNTS_MAP.getOrDefault(id, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findByPlayerId(Long playerId) {
        return ACCOUNTS_MAP.values().stream()
                .filter(a -> a.getPlayerId().equals(playerId))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Account> findAll() {
        return new ArrayList<>(ACCOUNTS_MAP.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            idCounter.incrementAndGet();
            account.setId(idCounter.longValue());
        }
        ACCOUNTS_MAP.put(account.getId(), account);
        return findById(account.getId()).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Account account) {
        ACCOUNTS_MAP.replace(account.getId(), account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Account account) {
        return ACCOUNTS_MAP.remove(account.getId(), account);
    }
}


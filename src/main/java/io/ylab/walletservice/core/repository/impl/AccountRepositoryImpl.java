package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * The {@link AccountRepositoryImpl} class implements the {@code AccountRepository} interface
 * for accessing and manipulating account entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRepositoryImpl implements AccountRepository<Long, Account> {

    private static final int COLLECTION_DEFAULT_CAPACITY = 10;
    public static final Map<Long, Account> accountsMap = new HashMap<>(COLLECTION_DEFAULT_CAPACITY);
    private static final AccountRepositoryImpl INSTANCE = new AccountRepositoryImpl();

    public static AccountRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountsMap.getOrDefault(id, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findByPlayerId(Long playerId) {
        return accountsMap.values().stream()
                .filter(a -> a.getPlayerId().equals(playerId))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Account> findAll() {
        return List.copyOf(accountsMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account save(Account account) {
        accountsMap.put(account.getId(), account);
        return findById(account.getId()).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Account account) {
        accountsMap.replace(account.getId(), account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Account account) {
        return accountsMap.remove(account.getId(), account);
    }
}


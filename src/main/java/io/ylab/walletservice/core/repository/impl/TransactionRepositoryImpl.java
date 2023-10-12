package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * The {@link TransactionRepositoryImpl} class implements the {@code TransactionRepository} interface
 * for accessing and manipulating transaction entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionRepositoryImpl implements TransactionRepository<Long> {

    private static final int COLLECTION_DEFAULT_CAPACITY = 10;
    private final Map<Long, Transaction> transactionsMap = new HashMap<>(COLLECTION_DEFAULT_CAPACITY);
    private static final TransactionRepositoryImpl INSTANCE = new TransactionRepositoryImpl();

    public static TransactionRepositoryImpl getInstance() {
        return INSTANCE;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(transactionsMap.getOrDefault(id, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to, Long playerId) {
        return transactionsMap.values().stream()
                .filter(t -> t.getParticipantAccount().getId().equals(playerId))
                .filter(t -> t.getCreatedAt().isAfter(from) && t.getCreatedAt().isBefore(to))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        return transactionsMap.values().stream()
                .filter(t -> t.getParticipantAccount().getId().equals(accountId))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAll() {
        return List.copyOf(transactionsMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction save(Transaction transaction) {
        return transactionsMap.putIfAbsent(transaction.getId(), transaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Transaction transaction) {
        return transactionsMap.remove(transaction.getId(), transaction);
    }
}

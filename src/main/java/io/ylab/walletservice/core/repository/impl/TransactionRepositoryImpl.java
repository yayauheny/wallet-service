package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * The {@link TransactionRepositoryImpl} class implements the {@code TransactionRepository} interface
 * for accessing and manipulating transaction entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionRepositoryImpl implements TransactionRepository<Long> {

    private static final int COLLECTION_DEFAULT_CAPACITY = 10;
    private static final Map<Long, Transaction> transactionsMap = new HashMap<>(COLLECTION_DEFAULT_CAPACITY);
    private static final TransactionRepositoryImpl INSTANCE = new TransactionRepositoryImpl();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

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
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        return transactionsMap.values().stream()
                .filter(t -> t.getParticipantAccount().getId().equals(accountId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactionsMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            idCounter.incrementAndGet();
            transaction.setId(idCounter.longValue());
        }
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


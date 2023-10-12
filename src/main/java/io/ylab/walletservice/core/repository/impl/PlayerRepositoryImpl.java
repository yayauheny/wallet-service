package io.ylab.walletservice.core.repository.impl;

import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.repository.PlayerRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The {@link PlayerRepositoryImpl} class implements the {@code PlayerRepository} interface
 * for accessing and manipulating player entities in a repository using an in-memory map.
 *
 * @NoArgsConstructor Creates an instance of the class with a private no-argument constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerRepositoryImpl implements PlayerRepository<Long, Player> {

    private static final int COLLECTION_DEFAULT_CAPACITY = 10;
    private final Map<Long, Player> playersMap = new HashMap<>(COLLECTION_DEFAULT_CAPACITY);
    private static final PlayerRepositoryImpl INSTANCE = new PlayerRepositoryImpl();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public static PlayerRepositoryImpl getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findById(Long id) {
        return Optional.ofNullable(playersMap.getOrDefault(id, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findByUsername(String username) {
        return playersMap.values().stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> findAll() {
        return List.copyOf(playersMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Player player) {
        if (player.getId() == null) {
            idCounter.incrementAndGet();
            player.setId(idCounter.longValue());
        }
        playersMap.putIfAbsent(player.getId(), player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Player player) {
        playersMap.replace(player.getId(), player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Player player) {
        return playersMap.remove(player.getId(), player);
    }
}


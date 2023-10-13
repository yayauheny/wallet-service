package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.service.PlayerService;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.exception.NotFoundException;
import lombok.AllArgsConstructor;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.repository.impl.PlayerRepositoryImpl;
import io.ylab.walletservice.api.Validator;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the {@link io.ylab.walletservice.core.service.PlayerService} interface providing
 * functionality to interact with player entities.
 */
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService<Long> {

    private final PlayerRepositoryImpl playerRepository;
    private final AccountServiceImpl accountService;

    public PlayerServiceImpl() {
        this.playerRepository = PlayerRepositoryImpl.getInstance();
        this.accountService = new AccountServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findById(Long id) {
        Validator.validateId(id);
        return playerRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getTransactions(Long playerId) throws NotFoundException {
        Validator.validateId(playerId);
        Optional<Account> maybeAccount = accountService.findByPlayerId(playerId);
        if (maybeAccount.isPresent()) {
            return maybeAccount.get().getTransactions();
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Player player) {
        playerRepository.update(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Player player) {
        return playerRepository.delete(player);
    }
}


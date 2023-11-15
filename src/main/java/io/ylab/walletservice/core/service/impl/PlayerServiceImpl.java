package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.api.Validator;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.repository.impl.PlayerRepositoryImpl;
import io.ylab.walletservice.core.service.PlayerService;
import io.ylab.walletservice.exception.DatabaseException;
import lombok.AllArgsConstructor;

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
    public Optional<Player> findById(Long id) throws DatabaseException {
        Validator.validateId(id);
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent()) {
            setDependencies(player.get());
        }
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findByUsername(String username) throws DatabaseException {
        Optional<Player> player = playerRepository.findByUsername(username);
        if (player.isPresent()) {
            setDependencies(player.get());
        }
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Player> findAll() throws DatabaseException {
        List<Player> players = playerRepository.findAll();
        for (Player player : players) {
            setDependencies(player);
        }
        return players;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getTransactions(Long playerId) throws DatabaseException {
        Validator.validateId(playerId);
        Optional<Account> maybeAccount = accountService.findByPlayerId(playerId);
        if (maybeAccount.isPresent()) {
            return maybeAccount.get().getTransactions();
        } else {
            throw new DatabaseException("Account not exists");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player save(Player player) throws DatabaseException {
        Player savedPlayer = playerRepository.save(player);
        setDependencies(savedPlayer);
        return savedPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Player player) throws DatabaseException {
        playerRepository.update(player);
        setDependencies(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Player player) throws DatabaseException {
        return playerRepository.delete(player);
    }

    private void setDependencies(Player player) throws DatabaseException {
        Optional<Account> account = accountService.findByPlayerId(player.getId());
        account.ifPresent(player::setAccount);
    }
}


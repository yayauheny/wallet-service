package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.api.PasswordHasher;
import io.ylab.walletservice.api.Validator;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.dto.player.PlayerCreateDto;
import io.ylab.walletservice.core.dto.player.PlayerUpdateDto;
import io.ylab.walletservice.core.mapper.PlayerMapper;
import io.ylab.walletservice.core.repository.impl.PlayerRepositoryImpl;
import io.ylab.walletservice.core.service.PlayerService;
import io.ylab.walletservice.exception.DatabaseException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
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
        player.ifPresent(this::setDependencies);
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Player> findByUsername(String username) throws DatabaseException {
        Optional<Player> player = playerRepository.findByUsername(username);
        player.ifPresent(this::setDependencies);
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
        Optional<Account> account = accountService.findByPlayerId(playerId);
        return account.isPresent()
                ? account.get().getTransactions()
                : new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player save(PlayerCreateDto request) throws DatabaseException {
        Player player = PlayerMapper.INSTANCE.fromPlayerCreateDto(request);
        Player savedPlayer = playerRepository.save(player);
        setDependencies(savedPlayer);
        return savedPlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PlayerUpdateDto request) throws DatabaseException {
        Player player = PlayerMapper.INSTANCE.fromRequest(request);
        playerRepository.update(player);
        setDependencies(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws DatabaseException {
        return playerRepository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyPassword(String password, String username) {
        Optional<Player> player = playerRepository.findByUsername(username);
        if (player.isPresent()) {
            byte[] existingPlayerPassword = player.get().getHashedPassword();
            return PasswordHasher.checkPassword(password, existingPlayerPassword);
        } else return false;
    }

    private void setDependencies(Player player) throws DatabaseException {
        Optional<Account> account = accountService.findByPlayerId(player.getId());
        account.ifPresent(player::setAccount);
    }
}


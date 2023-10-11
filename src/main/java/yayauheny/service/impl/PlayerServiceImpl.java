package yayauheny.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import yayauheny.entity.Account;
import yayauheny.entity.Player;
import yayauheny.entity.Transaction;
import yayauheny.exception.NotFoundException;
import yayauheny.repository.impl.PlayerRepositoryImpl;
import yayauheny.service.PlayerService;
import yayauheny.utils.Validator;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the {@link PlayerService} interface providing
 * functionality to interact with player entities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerServiceImpl implements PlayerService<Long> {

    /**
     * The singleton instance of the {@code PlayerServiceImpl}.
     */
    private static final PlayerServiceImpl INSTANCE = new PlayerServiceImpl();

    /**
     * The player repository for database interaction.
     */
    private final PlayerRepositoryImpl playerRepository = PlayerRepositoryImpl.getInstance();

    /**
     * The account service for managing account-related operations.
     */
    private final AccountServiceImpl accountService = AccountServiceImpl.getInstance();

    /**
     * Gets the singleton instance of the {@code PlayerServiceImpl}.
     *
     * @return The singleton instance.
     */
    public static PlayerServiceImpl getInstance() {
        return INSTANCE;
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
    public void save(Player player) {
        playerRepository.save(player);
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


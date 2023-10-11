package yayauheny.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import yayauheny.entity.Account;
import yayauheny.repository.impl.AccountRepositoryImpl;
import yayauheny.service.AccountService;
import yayauheny.utils.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


/**
 * The implementation of the {@link yayauheny.service.AccountService} interface.
 * Provides methods for interacting with {@link Account} entities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountServiceImpl implements AccountService<Long> {

    /**
     * The singleton instance of the {@code AccountServiceImpl} class.
     */
    private static final AccountServiceImpl INSTANCE = new AccountServiceImpl();

    /**
     * The {@link yayauheny.repository.impl.AccountRepositoryImpl} used for database operations.
     */
    private final AccountRepositoryImpl accountRepository = AccountRepositoryImpl.getInstance();

    /**
     * Returns the singleton instance of the {@code AccountServiceImpl} class.
     *
     * @return The singleton instance.
     */
    public static AccountServiceImpl getInstance() {
        return INSTANCE;
    }


    /**
     * Retrieves an {@link Account} by its unique identifier.
     *
     * @param id The unique identifier of the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws yayauheny.exception.InvalidIdException if the provided {@code id} is null or less than or equal to zero.
     */
    @Override
    public Optional<Account> findById(Long id) {
        Validator.validateId(id);
        return accountRepository.findById(id);
    }

    /**
     * Retrieves an {@link Account} by the player's unique identifier.
     *
     * @param playerId The unique identifier of the player associated with the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws yayauheny.exception.InvalidIdException if the provided {@code playerId} is null or less than or equal to zero.
     */
    @Override
    public Optional<Account> findByPlayerId(Long playerId) {
        Validator.validateId(playerId);
        return accountRepository.findByPlayerId(playerId);
    }

    /**
     * Retrieves all {@link Account} instances from repository.
     *
     *
     * @return A {@link List<Account>}, or an empty {@link List<Account>} if not found.
     */
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Account account) {
        accountRepository.update(account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBalance(Account account, BigDecimal updatedBalance) {
        account.setCurrentBalance(updatedBalance);
        update(account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Account account) {
        return accountRepository.delete(account);
    }

}

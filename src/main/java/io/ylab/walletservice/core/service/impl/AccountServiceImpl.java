package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.service.AccountService;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.repository.impl.AccountRepositoryImpl;
import io.ylab.walletservice.api.Validator;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


/**
 * The implementation of the {@link io.ylab.walletservice.core.service.AccountService} interface.
 * Provides methods for interacting with {@link io.ylab.walletservice.core.domain.Account} entities.
 */
@AllArgsConstructor
public class AccountServiceImpl implements AccountService<Long> {

    private static final Currency DEFAULT_CURRENCY = Currency.builder()
            .id(1L)
            .rate(BigDecimal.ONE)
            .code("USD")
            .build();
    private final AccountRepositoryImpl accountRepository;

    public AccountServiceImpl() {
        this.accountRepository = AccountRepositoryImpl.getInstance();
    }

    /**
     * Retrieves an {@link io.ylab.walletservice.core.domain.Account} by its unique identifier.
     *
     * @param id The unique identifier of the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws io.ylab.walletservice.exception.InvalidIdException if the provided {@code id} is null or less than or equal to zero.
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
     * @throws io.ylab.walletservice.exception.InvalidIdException if the provided {@code playerId} is null or less than or equal to zero.
     */
    @Override
    public Optional<Account> findByPlayerId(Long playerId) {
        Validator.validateId(playerId);
        return accountRepository.findByPlayerId(playerId);
    }

    /**
     * Retrieves all {@link Account} instances from repository.
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
        account.setCurrency(DEFAULT_CURRENCY);
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
        Validator.validateAmount(updatedBalance);
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

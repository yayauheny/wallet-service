package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.dto.account.AccountCreateDto;
import io.ylab.walletservice.core.dto.account.AccountResponse;
import io.ylab.walletservice.core.dto.account.AccountUpdateDto;
import io.ylab.walletservice.core.mapper.AccountMapper;
import io.ylab.walletservice.core.service.AccountService;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.repository.impl.AccountRepositoryImpl;
import io.ylab.walletservice.api.Validator;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.exception.InvalidIdException;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The implementation of the {@link AccountService} interface.
 * Provides methods for interacting with {@link Account} entities.
 */
@AllArgsConstructor
public class AccountServiceImpl implements AccountService<Long> {

    private final CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
    private final AccountRepositoryImpl accountRepository;

    public AccountServiceImpl() {
        this.accountRepository = AccountRepositoryImpl.getInstance();
    }

    /**
     * Retrieves an {@link Account} by its unique identifier.
     *
     * @param id The unique identifier of the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws InvalidIdException if the provided {@code id} is null or less than or equal to zero.
     */
    @Override
    public Optional<Account> findById(Long id) throws DatabaseException {
        Validator.validateId(id);
        Optional<Account> account = accountRepository.findById(id);
        account.ifPresent(this::setDependencies);
        return account;
    }

    /**
     * Retrieves an {@link Account} by the player's unique identifier.
     *
     * @param playerId The unique identifier of the player associated with the account to find.
     * @return An {@link Optional} containing the found account, or an empty {@link Optional} if not found.
     * @throws InvalidIdException if the provided {@code playerId} is null or less than or equal to zero.
     */
    @Override
    public Optional<Account> findByPlayerId(Long playerId) throws DatabaseException {
        Validator.validateId(playerId);
        Optional<Account> account = accountRepository.findByPlayerId(playerId);
        account.ifPresent(this::setDependencies);
        return account;
    }

    /**
     * Retrieves all {@link Account} instances from repository.
     *
     * @return A {@link List<Account>}, or an empty {@link List<Account>} if not found.
     */
    @Override
    public List<Account> findAll() throws DatabaseException {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            setDependencies(account);
        }
        return accounts;
    }

    @Override
    public List<Transaction> findAllTransactions(Account account) throws DatabaseException {
        List<Transaction> transactions = accountRepository.findAllTransactions(account.getId());
        for (Transaction transaction : transactions) {
            Optional<Currency> currency = currencyService.findByCode(transaction.getCurrencyCode());
            transaction.setParticipantAccount(account);
            currency.ifPresent(transaction::setCurrency);
        }
        return transactions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account save(AccountCreateDto accountDto) throws DatabaseException {
        Account savedAccount = accountRepository.save(AccountMapper.INSTANCE.fromRequest(accountDto));
        setDependencies(savedAccount);
        return savedAccount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(AccountUpdateDto dto) throws DatabaseException {
        Account account = AccountMapper.INSTANCE.fromUpdateDto(dto);
        accountRepository.update(account);
        setDependencies(account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBalance(AccountUpdateDto dto, BigDecimal updatedBalance) throws DatabaseException {
        Validator.validateAmount(updatedBalance);
        Account account = AccountMapper.INSTANCE.fromUpdateDto(dto);
        account.setCurrentBalance(updatedBalance);
        AccountMapper.INSTANCE.toUpdateDto(account);
        update(dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws DatabaseException {
        return accountRepository.delete(id);
    }

    private void setDependencies(Account account) throws DatabaseException {
        Optional<Currency> currency = currencyService.findByCode(account.getCurrencyCode());
        List<Transaction> transactions = findAllTransactions(account);
        currency.ifPresent(account::setCurrency);
        account.setTransactions(transactions);
    }
}

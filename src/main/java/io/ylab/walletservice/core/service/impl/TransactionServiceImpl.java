package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.dto.account.AccountCreateDto;
import io.ylab.walletservice.core.dto.account.AccountResponse;
import io.ylab.walletservice.core.dto.account.AccountUpdateDto;
import io.ylab.walletservice.core.dto.transaction.TransactionRequest;
import io.ylab.walletservice.core.mapper.AccountMapper;
import io.ylab.walletservice.core.mapper.TransactionMapper;
import io.ylab.walletservice.exception.DatabaseException;
import lombok.AllArgsConstructor;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.repository.impl.TransactionRepositoryImpl;
import io.ylab.walletservice.core.service.TransactionService;
import io.ylab.walletservice.api.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Implementation of the {@link TransactionService} interface providing
 * functionality to interact with transaction entities.
 */
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService<Long> {

    private final TransactionRepositoryImpl transactionRepository;
    private AccountServiceImpl accountService;
    private final CurrencyServiceImpl currencyService;

    public TransactionServiceImpl() {
        this.transactionRepository = TransactionRepositoryImpl.getInstance();
        this.currencyService = new CurrencyServiceImpl();
        this.accountService = new AccountServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Transaction> findById(Long id) throws DatabaseException {
        Validator.validateId(id);

        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            setDependencies(transaction.get());
        }
        return transaction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to, Long accountId) throws DatabaseException {
        Validator.validateId(accountId);
        Validator.validateTransactionsPeriod(from, to);

        List<Transaction> transactions = transactionRepository.findByPeriod(from, to, accountId);
        for (Transaction transaction : transactions) {
            setDependencies(transaction);
        }
        return transactions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAll() throws DatabaseException {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            setDependencies(transaction);
        }
        return transactions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAllByAccountId(Long accountId) throws DatabaseException {
        Validator.validateId(accountId);

        List<Transaction> transactions = transactionRepository.findAllByAccountId(accountId);
        for (Transaction transaction : transactions) {
            setDependencies(transaction);
        }
        return transactions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction save(Transaction transaction, Account account) throws DatabaseException {
        transaction.setParticipantAccount(account);
        transaction.setParticipantAccountId(account.getId());
        Validator.validateTransaction(transaction);
        account.getTransactions().add(transaction);

        Transaction savedTransaction = transactionRepository.save(transaction);
        setDependencies(savedTransaction);
        return savedTransaction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTransactionAndUpdateAccount(TransactionRequest transactionDto, AccountUpdateDto accountDto) throws DatabaseException {
        Transaction transaction = TransactionMapper.INSTANCE.fromRequest(transactionDto);
        Account account = AccountMapper.INSTANCE.fromUpdateDto(accountDto);
        transaction.setParticipantAccount(account);
        Validator.validateTransaction(transaction);

        BigDecimal updatedBalance;
        switch (transaction.getType()) {
            case CREDIT -> updatedBalance = account.getCurrentBalance().add(transaction.getAmount());
            case DEBIT -> updatedBalance = account.getCurrentBalance().subtract(transaction.getAmount());
            default -> updatedBalance = account.getCurrentBalance();
        }
        transactionRepository.save(transaction);
        accountService.updateBalance(accountDto, updatedBalance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws DatabaseException {
        return transactionRepository.delete(id);
    }

    private void setDependencies(Transaction transaction) throws DatabaseException {
        Optional<Account> participantAccount = accountService.findById(transaction.getParticipantAccountId());
        Optional<Currency> currency = currencyService.findByCode(transaction.getCurrencyCode());
        participantAccount.ifPresent(transaction::setParticipantAccount);
        currency.ifPresent(transaction::setCurrency);
    }
}


package io.ylab.walletservice.core.service.impl;

import io.ylab.walletservice.core.domain.Transaction;
import lombok.AllArgsConstructor;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.exception.TransactionException;
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
    private final AccountServiceImpl accountService;

    public TransactionServiceImpl() {
        this.transactionRepository = TransactionRepositoryImpl.getInstance();
        this.accountService = new AccountServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Transaction> findById(Long id) {
        Validator.validateId(id);
        return transactionRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to, Long accountId) {
        Validator.validateId(accountId);
        Validator.validateTransactionsPeriod(from, to);
        return transactionRepository.findByPeriod(from, to, accountId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAllByAccountId(Long accountId) {
        Validator.validateId(accountId);
        return transactionRepository.findAllByAccountId(accountId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction save(Transaction transaction, Account account) {
        transaction.setParticipantAccount(account);
        Validator.validateTransaction(transaction);
        account.getTransactions().add(transaction);
        return transactionRepository.save(transaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processTransactionAndUpdateAccount(Transaction transaction, Account account) {
        transaction.setParticipantAccount(account);
        Validator.validateTransaction(transaction);
        Optional<Transaction> maybeTransaction = findById(transaction.getId());
        if (maybeTransaction.isPresent()) {
            throw new TransactionException("Transaction id is not unique, please, provide another id");
        }
        BigDecimal updatedBalance;
        switch (transaction.getType()) {
            case CREDIT -> updatedBalance = account.getCurrentBalance().add(transaction.getAmount());
            case DEBIT -> updatedBalance = account.getCurrentBalance().subtract(transaction.getAmount());
            default -> updatedBalance = account.getCurrentBalance();
        }
        transactionRepository.save(transaction);
        accountService.updateBalance(account, updatedBalance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Transaction transaction) {
        return transactionRepository.delete(transaction);
    }
}


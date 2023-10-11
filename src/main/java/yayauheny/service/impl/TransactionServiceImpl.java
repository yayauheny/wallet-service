package yayauheny.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import yayauheny.entity.Account;
import yayauheny.entity.Transaction;
import yayauheny.repository.impl.TransactionRepositoryImpl;
import yayauheny.service.TransactionService;
import yayauheny.utils.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Implementation of the {@link TransactionService} interface providing
 * functionality to interact with transaction entities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionServiceImpl implements TransactionService<Long> {

    /**
     * The singleton instance of the {@code TransactionServiceImpl}.
     */
    private static final TransactionServiceImpl INSTANCE = new TransactionServiceImpl();

    /**
     * The transaction repository for database interaction.
     */
    private final TransactionRepositoryImpl transactionRepository = TransactionRepositoryImpl.getInstance();

    /**
     * The account service for managing account-related operations.
     */
    private final AccountServiceImpl accountService = AccountServiceImpl.getInstance();

    /**
     * Gets the singleton instance of the {@code TransactionServiceImpl}.
     *
     * @return The singleton instance.
     */
    public static TransactionServiceImpl getInstance() {
        return INSTANCE;
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
        return transactionRepository.findByAccountId(accountId);
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

        BigDecimal updatedBalance = account.getCurrentBalance().subtract(transaction.getAmount());
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


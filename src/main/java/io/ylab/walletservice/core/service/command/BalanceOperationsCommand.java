package io.ylab.walletservice.core.service.command;

import io.ylab.walletservice.api.Auditor;
import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.core.service.impl.TransactionServiceImpl;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.exception.TransactionException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.InputMismatchException;

/**
 * The {@link BalanceOperationsCommand} class is an implementation of the {@link Command} interface,
 * allowing users to perform balance operations such as crediting and debiting funds.
 */
public class BalanceOperationsCommand implements Command {

    private static final TransactionServiceImpl transactionService = new TransactionServiceImpl();

    /**
     * Executes the balance operations command for the specified player.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) {
        System.out.println("Выберите операцию:\n1 - пополнение средств\n2 - снятие средств");
        try {
            byte operation = Byte.parseByte(READER.readLine());
            switch (operation) {
                case 1 -> credit(player);
                case 2 -> debit(player);
            }
        } catch (InputMismatchException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            execute(player);
        }
    }

    /**
     * Gets the name of the balance operations command.
     *
     * @return The name of the command.
     */
    @Override
    public String getName() {
        return "Операции по счету";
    }

    /**
     * Initiates a debit operation for the specified player.
     *
     * @param player The player on which the debit operation is initiated.
     */
    private void debit(Player player) {
        buildTransaction(player, TransactionType.DEBIT);
    }

    /**
     * Initiates a credit operation for the specified player.
     *
     * @param player The player on which the credit operation is initiated.
     */
    private void credit(Player player) {
        buildTransaction(player, TransactionType.CREDIT);
    }

    /**
     * Builds a transaction based on user input.
     *
     * @param player          The account associated with the transaction.
     * @param transactionType The type of the transaction (debit or credit).
     */
    private void buildTransaction(Player player, TransactionType transactionType) {
        Account account = player.getAccount();

        switch (transactionType) {
            case DEBIT -> System.out.println(String.format("Введите сумму (%s) списания:", DEFAULT_CURRENCY.getCode()));
            case CREDIT ->
                    System.out.println(String.format("Введите сумму (%s) пополнения:", DEFAULT_CURRENCY.getCode()));
        }

        try {
            BigDecimal inputAmount = BigDecimal.valueOf(Long.parseLong(READER.readLine()));
            System.out.println("Введите уникальный номер транзакции (id)");
            long inputId = Long.parseLong(READER.readLine());
            Transaction transaction = Transaction.builder()
                    .id(inputId)
                    .amount(inputAmount)
                    .type(transactionType)
                    .participantAccount(account)
                    .currency(DEFAULT_CURRENCY)
                    .build();
            transactionService.processTransactionAndUpdateAccount(transaction, account);

            System.out.println("Транзакция прошла успешно, текущий баланс: " + account.getCurrentBalance());
            Auditor.log(String.format("player: %s committed transaction: %s",
                    player.getUsername(), transactionType + ": " + transaction.getAmount() + transaction.getCurrency().getCode()));
        } catch (InputMismatchException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            buildTransaction(player, transactionType);
        } catch (TransactionException e) {
            System.err.println(e.getMessage());
            execute(player);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}


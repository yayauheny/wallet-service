package yayauheny.service.command;

import yayauheny.entity.Account;
import yayauheny.entity.Player;
import yayauheny.entity.Transaction;
import yayauheny.entity.TransactionType;
import yayauheny.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * The {@link BalanceOperationsCommand} class is an implementation of the {@link Command} interface,
 * allowing users to perform balance operations such as crediting and debiting funds.
 */
public class BalanceOperationsCommand implements Command {

    /**
     * The singleton instance of the {@code TransactionServiceImpl}.
     */
    private static final TransactionServiceImpl transactionService = TransactionServiceImpl.getInstance();

    /**
     * Executes the balance operations command for the specified player.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) {
        System.out.println("Выберите операцию:\n1 - пополнение средств\n2 - снятие средств");
        try (Scanner sc = new Scanner(System.in)) {
            byte operation = sc.nextByte();
            switch (operation) {
                case 1 -> credit(player);
                case 2 -> debit(player);
            }
        } catch (InputMismatchException e) {
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
        buildTransaction(player.getAccount(), TransactionType.DEBIT);
    }

    /**
     * Initiates a credit operation for the specified player.
     *
     * @param player The player on which the credit operation is initiated.
     */
    private void credit(Player player) {
        buildTransaction(player.getAccount(), TransactionType.CREDIT);
    }

    /**
     * Builds a transaction based on user input.
     *
     * @param account         The account associated with the transaction.
     * @param transactionType The type of the transaction (debit or credit).
     */
    private void buildTransaction(Account account, TransactionType transactionType) {
        String currencyCode = account.getCurrency().getCode();
        switch (transactionType) {
            case DEBIT -> System.out.println(String.format("Введите сумму (%s) списания:", currencyCode));
            case CREDIT -> System.out.println(String.format("Введите сумму (%s) пополнения:", currencyCode));
        }

        try (Scanner sc = new Scanner(System.in)) {
            BigDecimal inputAmount = sc.nextBigDecimal();
            System.out.println("Введите уникальный номер транзакции (id)");
            long inputId = sc.nextLong();

            Transaction transaction = Transaction.builder()
                    .id(inputId)
                    .amount(inputAmount)
                    .type(transactionType)
                    .participantAccount(account)
                    .build();
            transactionService.processTransactionAndUpdateAccount(transaction, account);
            System.out.println("Транзакция прошла успешно, текущий баланс: " + account.getCurrentBalance());
        } catch (InputMismatchException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            buildTransaction(account, transactionType);
        }
    }
}


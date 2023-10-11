package yayauheny.service.command;

import yayauheny.entity.Account;
import yayauheny.entity.Currency;
import yayauheny.entity.Player;
import yayauheny.entity.Transaction;
import yayauheny.entity.TransactionType;
import yayauheny.exception.TransactionException;
import yayauheny.service.impl.TransactionServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.InputMismatchException;


/**
 * The {@link BalanceOperationsCommand} class is an implementation of the {@link Command} interface,
 * allowing users to perform balance operations such as crediting and debiting funds.
 */
public class BalanceOperationsCommand implements Command {

    private static final Currency DEFAULT_CURRENCY = new Currency(BigDecimal.ONE, "USD");

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
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            byte operation = Byte.parseByte(reader.readLine());
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
     * @param player         The account associated with the transaction.
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BigDecimal inputAmount = BigDecimal.valueOf(Long.parseLong(reader.readLine()));
            System.out.println("Введите уникальный номер транзакции (id)");
            long inputId = Long.parseLong(reader.readLine());

            Transaction transaction = Transaction.builder()
                    .id(inputId)
                    .amount(inputAmount)
                    .type(transactionType)
                    .participantAccount(account)
                    .currency(DEFAULT_CURRENCY)
                    .build();
            transactionService.processTransactionAndUpdateAccount(transaction, account);
            System.out.println("Транзакция прошла успешно, текущий баланс: " + account.getCurrentBalance());
        } catch (InputMismatchException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            buildTransaction(player, transactionType);
        } catch (TransactionException e){f
            System.err.println(e.getMessage());
            execute(player);
        }
    }
}


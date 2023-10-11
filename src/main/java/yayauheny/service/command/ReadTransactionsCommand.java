package yayauheny.service.command;

import yayauheny.entity.Player;
import yayauheny.entity.Receipt;
import yayauheny.service.impl.Auditor;
import yayauheny.service.impl.ReceiptService;
import yayauheny.service.impl.SummaryReceiptService;
import yayauheny.service.impl.TransactionServiceImpl;
import yayauheny.utils.DateTimeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * The {@link ReadTransactionsCommand} class is an implementation of the {@link Command} interface,
 * allowing users to retrieve a list of transactions based on the chosen period.
 */
public class ReadTransactionsCommand implements Command {

    private static final TransactionServiceImpl transactionService = TransactionServiceImpl.getInstance();

    /**
     * Executes the command to retrieve a list of transactions based on the chosen period.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) {
        System.out.println("Выберите период выписки:\n1 - ввести вручную\n2 - все транзакции");
        try (Scanner sc = new Scanner(System.in)) {
            byte choice = sc.nextByte();
            Receipt receipt;
            switch (choice) {
                case 1 -> receipt = readTransactionsByPeriod(player);
                case 2 -> receipt = readAllTransactions(player);
                default -> throw new InputMismatchException();
            }
            ReceiptService receiptService = SummaryReceiptService.getInstance();
            System.out.println(receiptService.buildReceipt(receipt));
            Auditor.log("player: %s created receipt".formatted(player.getUsername()));
        } catch (InputMismatchException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            execute(player);
        }
    }

    /**
     * Gets the name of the read transactions command.
     *
     * @return The name of the command.
     */
    @Override
    public String getName() {
        return "Получение списка транзакций";
    }

    private Receipt readTransactionsByPeriod(Player player) {
        System.out.println("Введите дату начала истории транзакций (гггг.мм.дд):");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String inputDateFrom = reader.readLine();
            LocalDateTime dateFrom = LocalDateTime.parse(inputDateFrom, DateTimeUtils.dateFormatter);
            System.out.println("Введите дату конца истории транзакций (гггг.мм.дд):");
            String inputDateTo = reader.readLine();
            LocalDateTime dateTo = LocalDateTime.parse(inputDateTo, DateTimeUtils.dateFormatter);

            return new Receipt(player.getAccount(), player, dateFrom, dateTo);
        } catch (InputMismatchException | NumberFormatException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            return readTransactionsByPeriod(player);
        }
    }

    private Receipt readAllTransactions(Player player) {
        LocalDate createdAt = player.getAccount().getCreatedAt();
        return new Receipt(player.getAccount(), player, LocalDateTime.of(createdAt, LocalTime.MIDNIGHT), LocalDateTime.now());
    }
}


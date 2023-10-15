package io.ylab.walletservice.core.service.command;

import io.ylab.walletservice.api.Auditor;
import io.ylab.walletservice.core.domain.Player;
import io.ylab.walletservice.core.domain.Receipt;
import io.ylab.walletservice.core.service.receipt.ReceiptService;
import io.ylab.walletservice.core.service.receipt.SummaryReceiptService;
import io.ylab.walletservice.exception.ReceiptBuildingException;
import io.ylab.walletservice.util.DateTimeUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;


/**
 * The {@link ReadTransactionsCommand} class is an implementation of the {@link Command} interface,
 * allowing users to retrieve a list of transactions based on the chosen period.
 */
public class ReadTransactionsCommand implements Command {

    /**
     * Executes the command to retrieve a list of transactions based on the chosen period.
     *
     * @param player The player on which the command is executed.
     */
    @Override
    public void execute(Player player) {
        System.out.println("Выберите период выписки:\n1 - ввести вручную\n2 - все транзакции");
        try {
            int choice = Integer.parseInt(READER.readLine());
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
        } catch (ReceiptBuildingException | IOException e) {
            System.err.println("Ошибка создания чека, попробуйте позже");
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
            String inputDateFrom = READER.readLine();
            LocalDateTime dateFrom = LocalDateTime.parse(inputDateFrom, DateTimeUtils.dateFormatter);
            System.out.println("Введите дату конца истории транзакций (гггг.мм.дд):");
            String inputDateTo = READER.readLine();
            LocalDateTime dateTo = LocalDateTime.parse(inputDateTo, DateTimeUtils.dateFormatter);

            return new Receipt(player.getAccount(), player, dateFrom, dateTo);
        } catch (InputMismatchException | NumberFormatException | IOException e) {
            System.err.println("Некорректный ввод, попробуйте снова");
            return readTransactionsByPeriod(player);
        }
    }

    private Receipt readAllTransactions(Player player) {
        LocalDateTime createdAt = player.getAccount().getCreatedAt();
        return new Receipt(player.getAccount(), player, createdAt, LocalDateTime.now());
    }
}


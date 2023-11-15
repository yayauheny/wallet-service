package io.ylab.walletservice.core.service.receipt;

import io.ylab.walletservice.api.Validator;
import io.ylab.walletservice.core.dto.ReceiptDto;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.core.service.impl.TransactionServiceImpl;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.exception.ReceiptBuildingException;
import io.ylab.walletservice.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * An abstract class representing a service for building receipts.
 */
public abstract class ReceiptService {

    private static final TransactionServiceImpl transactionService = new TransactionServiceImpl();
    /**
     * A map to store transaction types and their corresponding descriptions.
     */
    public static final Map<TransactionType, String> TRANSACTION_TYPE_MAP = Map.ofEntries(
            Map.entry(TransactionType.DEBIT, "Снятие"),
            Map.entry(TransactionType.CREDIT, "Пополнение")
    );
    private static AtomicInteger receiptCounter = new AtomicInteger(0);

    /**
     * A separator used in the receipt template.
     */
    public static final String SEPARATOR = "-".repeat(65);

    /**
     * Builds a receipt using the provided template and saves it as a text file.
     *
     * @param receiptDto The receipt to build.
     * @return The completed receipt.
     * @throws ReceiptBuildingException if exception caused while saving receipt to the file.
     */
    public String buildReceipt(ReceiptDto receiptDto) throws DatabaseException {
        String completedReceipt = buildTemplate(receiptDto);
        saveReceiptAsTxt(completedReceipt);
        return completedReceipt;
    }

    /**
     * Saves the receipt as a text file.
     *
     * @param receipt The receipt to save.
     * @throws ReceiptBuildingException if exception caused while saving receipt to the file.
     */
    private void saveReceiptAsTxt(String receipt) {
        try {
            Path directoryPath = Paths.get("tickets").toAbsolutePath();
            Files.createDirectories(directoryPath);

            Path filePath = Path.of(String.format("tickets/ticket_%s.txt", receiptCounter.incrementAndGet()));

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
            Files.writeString(filePath, receipt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ReceiptBuildingException("Cannot write receipt to the file");
        }
    }

    /**
     * Builds the template for the receipt.
     *
     * @param receiptDto The receipt to build the template for.
     * @return The receipt template.
     */
    public abstract String buildTemplate(ReceiptDto receiptDto) throws DatabaseException;

    /**
     * Builds the body of the transaction section in the receipt.
     *
     * @param receiptDto The receipt containing transactions.
     * @return The transaction body.
     */
    public String buildTransactionBody(ReceiptDto receiptDto) throws DatabaseException {
        Validator.validateReceiptForMoneyStatement(receiptDto);
        List<Transaction> transactions = transactionService.findByPeriod(receiptDto.from(), receiptDto.to(), receiptDto.account().getId());
        String transactionEnd = buildTransactionEnd(transactions);
        return """
                %s | %s | %s
                %s
                %s
                """.formatted(StringUtils.center("Дата", 12),
                StringUtils.center("Примечание", 39),
                StringUtils.rightPad("Сумма", 16),
                SEPARATOR,
                transactionEnd);
    }

    /**
     * Builds the end section of the transaction in the receipt.
     *
     * @param transactions The list of transactions.
     * @return The transaction end section.
     */
    private String buildTransactionEnd(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        for (Transaction transaction : transactions) {
            String transactionTemplate = "%s | %s | %s\n";
            String positiveAmount = StringUtils.rightPad(transaction.getAmount()
                                                         + " " + transaction.getCurrency().getCode(), 20);
            String negativeAmount = StringUtils.rightPad(transaction.getAmount().negate()
                                                         + " " + transaction.getCurrency().getCode(), 20);
            switch (transaction.getType()) {
                case CREDIT ->
                        sb.append(transactionTemplate.formatted(StringUtils.center(DateTimeUtils.parseDate(transaction.getCreatedAt()), 12),
                                StringUtils.rightPad(TRANSACTION_TYPE_MAP.get(TransactionType.CREDIT), 39),
                                positiveAmount));
                case DEBIT ->
                        sb.append(transactionTemplate.formatted(StringUtils.center(DateTimeUtils.parseDate(transaction.getCreatedAt()), 12),
                                StringUtils.rightPad(TRANSACTION_TYPE_MAP.get(TransactionType.DEBIT), 48),
                                negativeAmount));
            }
        }
        return sb.toString();
    }
}


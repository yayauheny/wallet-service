package io.ylab.walletservice.core.service.receipt;

import io.ylab.walletservice.core.service.impl.TransactionServiceImpl;
import io.ylab.walletservice.core.domain.Receipt;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import io.ylab.walletservice.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import io.ylab.walletservice.api.Validator;

import java.util.List;
import java.util.Map;


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

    /**
     * A separator used in the receipt template.
     */
    public static final String SEPARATOR = "-".repeat(65);

    /**
     * Builds a receipt using the provided template and saves it as a text file.
     *
     * @param receipt The receipt to build.
     * @return The completed receipt.
     */
    public String buildReceipt(Receipt receipt) {
        String completedReceipt = buildTemplate(receipt);
        saveReceiptAsTxt(completedReceipt);
        return completedReceipt;
    }

    /**
     * Saves the receipt as a text file.
     *
     * @param receipt The receipt to save.
     */
    private void saveReceiptAsTxt(String receipt) {
        // Perform the save operation.
    }

    /**
     * Builds the template for the receipt.
     *
     * @param receipt The receipt to build the template for.
     * @return The receipt template.
     */
    public abstract String buildTemplate(Receipt receipt);

    /**
     * Builds the body of the transaction section in the receipt.
     *
     * @param receipt The receipt containing transactions.
     * @return The transaction body.
     */
    public String buildTransactionBody(Receipt receipt) {
        Validator.validateReceiptForMoneyStatement(receipt);
        List<Transaction> transactions = transactionService.findByPeriod(receipt.from(), receipt.to(), receipt.account().getId());
        String transactionEnd = buildTransactionEnd(transactions);
        return """
                %s | %s | %s
                %s
                %s
                """.formatted(StringUtils.center("Дата", 16),
                StringUtils.center("Примечание", 49),
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
                                StringUtils.rightPad(TRANSACTION_TYPE_MAP.get(TransactionType.CREDIT), 48),
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


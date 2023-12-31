package io.ylab.walletservice.core.service.receipt;

import io.ylab.walletservice.core.dto.receipt.ReceiptDto;
import io.ylab.walletservice.exception.DatabaseException;
import io.ylab.walletservice.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;


/**
 * A service for building summary receipts, extending the base {@link ReceiptService}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SummaryReceiptService extends ReceiptService {

    /**
     * The singleton instance of the {@code SummaryReceiptService}.
     */
    private static final SummaryReceiptService INSTANCE = new SummaryReceiptService();

    /**
     * Gets the singleton instance of the {@code SummaryReceiptService}.
     *
     * @return The singleton instance.
     */
    public static SummaryReceiptService getInstance() {
        return INSTANCE;
    }

    /**
     * Builds the template for a summary receipt.
     *
     * @param receiptDto The receipt to build the template for.
     * @return The summary receipt template.
     */
    @Override
    public String buildTemplate(ReceiptDto receiptDto) throws DatabaseException {
        return """
                %s
                %s
                %s : %s
                %s : %s
                %s : %s
                %s
                %s
                """.formatted(StringUtils.center("История транзакций", 70),
                SEPARATOR,
                StringUtils.rightPad("Игрок", 39),
                receiptDto.player().getUsername(),
                StringUtils.rightPad("Период", 39),
                (DateTimeUtils.parseDate(receiptDto.from()) + " - " + DateTimeUtils.parseDate(receiptDto.to())),
                StringUtils.rightPad("Дата и время формирования", 39),
                (DateTimeUtils.parseCurrentDate() + ", " + DateTimeUtils.parseCurrentTime()),
                SEPARATOR,
                buildTransactionBody(receiptDto)
        );
    }
}


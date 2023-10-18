package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.domain.Transaction;
import io.ylab.walletservice.core.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aTransaction")
@With
public class TransactionTestBuilder implements TestBuilder<Transaction> {

    private Long id = 0L;
    private TransactionType type;
    private BigDecimal amount = BigDecimal.ZERO;
    private Currency currency = TestObjectsUtil.TEST_CURRENCY_USD;
    private String currencyCode;
    private LocalDateTime createdAt;
    private Long participantAccountId;
    private Account participantAccount;

    @Override
    public Transaction build() {
        return Transaction.builder()
                .id(id)
                .type(type)
                .amount(amount)
                .currency(currency)
                .createdAt(createdAt)
                .participantAccountId(participantAccountId)
                .participantAccount(participantAccount)
                .currencyCode(currencyCode)
                .build();
    }
}
package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;
import io.ylab.walletservice.core.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link AccountTestBuilder} class is a test utility for creating instances of the {@link Account} class
 * with customizable or default attribute values. It implements the {@link TestBuilder} interface to provide
 * a standardized way of constructing {@link Account} objects for testing scenarios.
 */
@AllArgsConstructor
@NoArgsConstructor(staticName = "anAccount")
@With
public class AccountTestBuilder implements TestBuilder<Account> {

    private Long id = 0L;
    private Long playerId = 0L;
    private String currencyCode = "USD";
    private BigDecimal currentBalance = BigDecimal.ZERO;
    private Currency currency;
    private List<Transaction> transactions = new ArrayList<>();
    private LocalDateTime createdAt;

    /**
     * {@inheritDoc}
     */
    @Override
    public Account build() {
        return Account.builder()
                .id(id)
                .currentBalance(currentBalance)
                .playerId(playerId)
                .currencyCode(currencyCode)
                .currency(currency)
                .transactions(transactions)
                .createdAt(createdAt)
                .build();
    }
}
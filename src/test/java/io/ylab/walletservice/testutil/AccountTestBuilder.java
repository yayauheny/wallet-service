package io.ylab.walletservice.testutil;

import io.ylab.walletservice.core.domain.Account;
import io.ylab.walletservice.core.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

import static io.ylab.walletservice.testutil.TestObjectsUtil.TEST_CURRENCY;


@AllArgsConstructor
@NoArgsConstructor(staticName = "anAccount")
@With
public class AccountTestBuilder implements TestBuilder<Account> {

    private Long id = 0L;
    private Long playerId = 0L;
    private BigDecimal currentBalance = BigDecimal.ZERO;
    private Currency currency = TEST_CURRENCY;

    @Override
    public Account build() {
        return Account.builder()
                .id(id)
                .currentBalance(currentBalance)
                .playerId(playerId)
                .currency(currency)
                .build();
    }
}
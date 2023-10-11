package yayauheny.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;
    private LocalDateTime createdAt;
    private TransactionType type;
    private BigDecimal amount;
    private Account participantAccount;
    private Currency currency;

    @Builder
    public Transaction(Long id, TransactionType type, BigDecimal amount, Account participantAccount, Currency currency) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.participantAccount = participantAccount;
        this.currency = currency;
    }
}

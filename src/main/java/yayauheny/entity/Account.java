package yayauheny.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private static long idCounter = 0;
    private Long id;
    private BigDecimal currentBalance = BigDecimal.ZERO;
    private List<Transaction> transactions = new ArrayList<>();
    private Currency currency;
    private Long playerId;
    private LocalDate createdAt;

    @Builder
    public Account(List<Transaction> transactions, Currency currency, Long playerId) {
        this.id = idCounter++;
        this.transactions = transactions;
        this.currency = currency;
        this.playerId = playerId;
        this.createdAt = LocalDate.now();
    }
}

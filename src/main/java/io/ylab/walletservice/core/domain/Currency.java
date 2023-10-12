package io.ylab.walletservice.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private static long idCounter = 0;
    private Long id;
    private BigDecimal rate;
    private String code;

    @Builder
    public Currency(BigDecimal rate, String code) {
        this.id = idCounter++;
        this.rate = rate;
        this.code = code;
    }
}
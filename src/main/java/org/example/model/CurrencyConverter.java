package org.example.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {
    public BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (from == to) {
            return amount;
        }

        BigDecimal amountInAzn = amount.multiply(rateToAzn(from));
        return amountInAzn.divide(rateToAzn(to), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal rateToAzn(Currency currency) {
        return switch (currency) {
            case AZN -> BigDecimal.ONE;
            case USD -> new BigDecimal("1.70");
            case EUR -> new BigDecimal("1.82");
        };
    }
}
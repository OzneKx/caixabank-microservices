package com.caixabank.transaction.dtos;

import com.caixabank.transaction.data.enums.AccountType;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String accountNumber,
        AccountType accountType,
        BigDecimal balance
) {
}

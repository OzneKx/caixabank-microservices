package com.caixabank.account.dto;

import com.caixabank.account.data.enums.AccountType;

import java.math.BigDecimal;

public record AccountResponse(
        String accountNumber,
        AccountType accountType,
        BigDecimal balance
) {
}

package com.caixabank.account.dto;

import com.caixabank.account.data.enums.AccountType;
import jakarta.validation.constraints.NotNull;

public record AccountCreateRequest(
        @NotNull(message = "Account type is required.")
        AccountType accountType
) {
}

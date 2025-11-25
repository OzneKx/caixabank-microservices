package com.caixabank.transaction.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "Destination account ID is required.")
        Long destinationAccountId,

        @NotNull
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero.")
        BigDecimal amount
) {}

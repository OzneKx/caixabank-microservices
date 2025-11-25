package com.caixabank.transaction.dtos;

import com.caixabank.transaction.data.enums.TransactionStatus;
import com.caixabank.transaction.data.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long userId,
        Long accountOriginId,
        Long accountDestinationId,
        BigDecimal amount,
        TransactionType transactionType,
        TransactionStatus transactionStatus,
        LocalDateTime createdAt
) {
}

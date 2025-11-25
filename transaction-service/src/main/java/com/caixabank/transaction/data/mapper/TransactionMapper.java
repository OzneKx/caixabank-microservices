package com.caixabank.transaction.data.mapper;

import com.caixabank.transaction.data.entity.Transaction;
import com.caixabank.transaction.dtos.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction transaction);
}

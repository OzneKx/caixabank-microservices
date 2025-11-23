package com.caixabank.account.data.mapper;

import com.caixabank.account.data.entity.Account;
import com.caixabank.account.dto.AccountCreateRequest;
import com.caixabank.account.dto.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(source = "accountType", target = "accountType")
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Account toEntity(AccountCreateRequest accountCreateRequest);

    AccountResponse toResponse(Account account);
}

package com.caixabank.account.service;

import com.caixabank.account.data.entity.Account;
import com.caixabank.account.data.enums.AccountType;
import com.caixabank.account.data.mapper.AccountMapper;
import com.caixabank.account.data.repository.AccountRepository;
import com.caixabank.account.dto.AccountCreateRequest;
import com.caixabank.account.dto.AccountResponse;
import com.caixabank.account.exception.ForbiddenAccessException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public List<AccountResponse> findAllAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);

        if (accounts.isEmpty()) {
            throw new EntityNotFoundException("No accounts found for this user.");
        }

        return accounts.stream().map(accountMapper::toResponse).toList();
    }

    public AccountResponse findMainAccount(Long userId) {
        Account mainAccount = accountRepository.findByUserIdAndAccountType(userId, AccountType.MAIN)
                .orElseThrow(() -> new EntityNotFoundException("Main account not found."));

        return accountMapper.toResponse(mainAccount);
    }

    public AccountResponse findById(Long id, Long userId) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found."));

        if (!account.getUserId().equals(userId)) {
            throw new ForbiddenAccessException("You do not have permission to access this account.");
        }

        return accountMapper.toResponse(account);
    }

    public AccountResponse createAccount(Long userId, AccountCreateRequest accountCreateRequest) {
        Account account = accountMapper.toEntity(accountCreateRequest);

        account.setId(null);
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateAccountNumber());

        Account saved = accountRepository.save(account);
        return accountMapper.toResponse(saved);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}

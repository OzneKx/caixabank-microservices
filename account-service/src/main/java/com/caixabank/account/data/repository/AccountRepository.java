package com.caixabank.account.data.repository;

import com.caixabank.account.data.entity.Account;
import com.caixabank.account.data.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserIdAndAccountType(Long userId, AccountType accountType);
    List<Account> findAllByUserId(Long userId);
}

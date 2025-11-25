package com.caixabank.transaction.data.repository;

import com.caixabank.transaction.data.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    List<Transaction> findAllByAccountOriginIdOrderByCreatedAtDesc(Long accountOriginId);
}

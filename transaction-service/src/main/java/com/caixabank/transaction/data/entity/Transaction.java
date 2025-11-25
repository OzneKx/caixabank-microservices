package com.caixabank.transaction.data.entity;

import com.caixabank.transaction.data.enums.TransactionStatus;
import com.caixabank.transaction.data.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(name = "account_origin_id")
    Long accountOriginId;

    @Column(name = "account_destination_id")
    Long accountDestinationId;

    @Column(nullable = false, precision = 20, scale = 2)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    TransactionStatus transactionStatus;

    @Column(nullable = false)
    LocalDateTime createdAt;
}

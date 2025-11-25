package com.caixabank.transaction.service.impl;

import com.caixabank.transaction.client.AccountClient;
import com.caixabank.transaction.data.entity.Transaction;
import com.caixabank.transaction.data.enums.TransactionStatus;
import com.caixabank.transaction.data.enums.TransactionType;
import com.caixabank.transaction.data.mapper.TransactionMapper;
import com.caixabank.transaction.data.repository.TransactionRepository;
import com.caixabank.transaction.dtos.AccountResponse;
import com.caixabank.transaction.dtos.DepositRequest;
import com.caixabank.transaction.dtos.MessageResponse;
import com.caixabank.transaction.dtos.TransactionResponse;
import com.caixabank.transaction.dtos.TransferRequest;
import com.caixabank.transaction.dtos.WithdrawRequest;
import com.caixabank.transaction.exception.InsufficientBalanceException;
import com.caixabank.transaction.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountClient accountClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  TransactionMapper transactionMapper,
                                  AccountClient accountClient) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountClient = accountClient;
    }

    @Override
    public MessageResponse cashDeposit(Long userId, DepositRequest depositRequest) {
        var mainAccount = checkIfMainAccountIsValid(userId);
        Transaction cashDepositTransaction = getCashDepositTransaction(userId, depositRequest, mainAccount);
        transactionRepository.save(cashDepositTransaction);
        return new MessageResponse("Deposit completed successfully.");
    }

    @Override
    public MessageResponse cashWithdraw(Long userId, WithdrawRequest withdrawRequest) {
        var mainAccount = checkIfMainAccountIsValid(userId);
        validateWithdrawTransaction(withdrawRequest, mainAccount);
        Transaction cashWithdrawTransaction = getCashWithdrawTransaction(userId, withdrawRequest, mainAccount);
        transactionRepository.save(cashWithdrawTransaction);
        return new MessageResponse("Withdraw completed successfully.");
    }


    @Override
    public MessageResponse cashTransfer(Long userId, TransferRequest transferRequest) {
        var mainAccount = checkIfMainAccountIsValid(userId);
        var destinationAccount = checkIfDestinationAccountIsValid(transferRequest.destinationAccountId());
        validateTransferTransaction(transferRequest, mainAccount);
        Transaction cashTransferTransaction = getCashTransferTransaction(userId, transferRequest, mainAccount, destinationAccount);
        transactionRepository.save(cashTransferTransaction);
        return new MessageResponse("Transfer completed successfully.");
    }

    @Override
    public List<TransactionResponse> getUserTransactions(Long userId) {
        return transactionRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    private AccountResponse checkIfMainAccountIsValid(Long userId) {
        var mainAccount = accountClient.getMainAccount(userId);

        if (mainAccount == null) {
            throw new EntityNotFoundException("Main account not found for this user.");
        }

        return mainAccount;
    }

    private AccountResponse checkIfDestinationAccountIsValid(Long destinationAccountId) {
        var destinationAccount = accountClient.getAccount(null, destinationAccountId);

        if (destinationAccount == null) {
            throw new EntityNotFoundException("Destination account not found.");
        }

        return destinationAccount;
    }

    private void validateWithdrawTransaction(WithdrawRequest withdrawRequest, AccountResponse mainAccount) {
        if (mainAccount.balance().compareTo(withdrawRequest.amount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
    }

    private void validateTransferTransaction(TransferRequest transferRequest, AccountResponse mainAccount) {
        if (mainAccount.balance().compareTo(transferRequest.amount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
    }

    private Transaction getCashDepositTransaction(Long userId, DepositRequest depositRequest,
                                                  AccountResponse mainAccount) {
        return Transaction.builder()
                .userId(userId)
                .accountOriginId(null)
                .accountDestinationId(mainAccount.id())
                .transactionType(TransactionType.CASH_DEPOSIT)
                .amount(depositRequest.amount())
                .transactionStatus(TransactionStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Transaction getCashWithdrawTransaction(Long userId, WithdrawRequest withdrawRequest,
                                                   AccountResponse mainAccount) {
        return Transaction.builder()
                .userId(userId)
                .accountOriginId(mainAccount.id())
                .accountDestinationId(null)
                .transactionType(TransactionType.CASH_WITHDRAW)
                .amount(withdrawRequest.amount())
                .transactionStatus(TransactionStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Transaction getCashTransferTransaction(Long userId, TransferRequest transferRequest,
                                                   AccountResponse mainAccount, AccountResponse destinationAccount) {
        return Transaction.builder()
                .userId(userId)
                .accountOriginId(mainAccount.id())
                .accountDestinationId(destinationAccount.id())
                .transactionType(TransactionType.CASH_TRANSFER)
                .amount(transferRequest.amount())
                .transactionStatus(TransactionStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

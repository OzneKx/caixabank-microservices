package com.caixabank.transaction.service;

import com.caixabank.transaction.dtos.DepositRequest;
import com.caixabank.transaction.dtos.MessageResponse;
import com.caixabank.transaction.dtos.TransactionResponse;
import com.caixabank.transaction.dtos.TransferRequest;
import com.caixabank.transaction.dtos.WithdrawRequest;

import java.util.List;

public interface TransactionService {
    MessageResponse cashDeposit(Long userId, DepositRequest depositRequest);

    MessageResponse cashWithdraw(Long userId, WithdrawRequest withdrawRequest);

    MessageResponse cashTransfer(Long userId, TransferRequest transferRequest);

    List<TransactionResponse> getUserTransactions(Long userId);
}

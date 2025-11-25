package com.caixabank.transaction.controller;

import com.caixabank.transaction.dtos.DepositRequest;
import com.caixabank.transaction.dtos.MessageResponse;
import com.caixabank.transaction.dtos.TransactionResponse;
import com.caixabank.transaction.dtos.TransferRequest;
import com.caixabank.transaction.dtos.WithdrawRequest;
import com.caixabank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<MessageResponse> deposit(@RequestHeader("X-User-Id") Long userId,
                                                   @Valid @RequestBody DepositRequest depositRequest) {
        MessageResponse messageResponse = transactionService.cashDeposit(userId, depositRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<MessageResponse> withdraw(@RequestHeader("X-User-Id") Long userId,
                                                    @Valid @RequestBody WithdrawRequest withdrawRequest) {
        MessageResponse messageResponse = transactionService.cashWithdraw(userId, withdrawRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity<MessageResponse> transfer(@RequestHeader("X-User-Id") Long userId,
                                                    @Valid @RequestBody TransferRequest transferRequest
    ) {
        MessageResponse messageResponse = transactionService.cashTransfer(userId, transferRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(@RequestHeader("X-User-Id") Long userId) {
        List<TransactionResponse> transactions = transactionService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }
}

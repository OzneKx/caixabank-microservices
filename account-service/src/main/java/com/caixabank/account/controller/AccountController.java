package com.caixabank.account.controller;

import com.caixabank.account.dto.AccountCreateRequest;
import com.caixabank.account.dto.AccountResponse;
import com.caixabank.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@RequestAttribute("userId") Long userId) {
        List<AccountResponse> accountResponse = accountService.findAllAccountsByUserId(userId);
        return ResponseEntity.ok().body(accountResponse);
    }

    @GetMapping("/main")
    public ResponseEntity<AccountResponse> getMainAccount(@RequestAttribute("userId") Long userId) {
        AccountResponse accountResponse = accountService.findMainAccount(userId);
        return ResponseEntity.ok().body(accountResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        AccountResponse accountResponse = accountService.findById(id, userId);
        return ResponseEntity.ok().body(accountResponse);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestAttribute("userId") Long userId,
                                                         @Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        AccountResponse accountResponse = accountService.createAccount(userId, accountCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }
}

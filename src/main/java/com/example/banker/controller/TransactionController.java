package com.example.banker.controller;

import com.example.banker.model.Account;
import com.example.banker.model.Transaction;
import com.example.banker.service.AccountService;
import com.example.banker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    // Get transactions by accountId passed as query param
    @GetMapping
    public ResponseEntity<List<Transaction>> getByAccount(@RequestParam Long accountId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Account> accountOpt = accountService.getById(accountId);
        if (accountOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountOpt.get();

        if (!account.getUser().getName().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId);
        return ResponseEntity.ok(transactions);
    }
}

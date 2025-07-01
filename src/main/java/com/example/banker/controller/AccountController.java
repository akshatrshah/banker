package com.example.banker.controller;

import com.example.banker.model.Account;
import com.example.banker.model.User;
import com.example.banker.service.AccountService;
import com.example.banker.service.TransactionService;
import com.example.banker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    // ✅ Create account for logged-in user
    @PostMapping("/create")
    public ResponseEntity<Account> create() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOpt = userService.findByName(username);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().build();

        Account account = accountService.createAccount(userOpt.get());
        return ResponseEntity.ok(account);
    }

    // ✅ Get account by accountId (query param)
    @GetMapping
    public ResponseEntity<Account> getById(@RequestParam Long accountId) {
        return accountService.getById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Deposit money (query params: accountId, amount)
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        Optional<Account> accountOpt = accountService.getById(accountId);
        if (accountOpt.isEmpty()) return ResponseEntity.badRequest().body("Account not found");

        Account account = accountOpt.get();
        account.setBalance(account.getBalance().add(amount));
        accountService.updateBalance(account, account.getBalance());

        transactionService.logTransaction(account, amount, "DEPOSIT"); // ✅ Log transaction

        return ResponseEntity.ok("Deposited successfully");
    }

    // ✅ Withdraw money (query params: accountId, amount)
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        Optional<Account> accountOpt = accountService.getById(accountId);
        if (accountOpt.isEmpty()) return ResponseEntity.badRequest().body("Account not found");

        Account account = accountOpt.get();
        if (account.getBalance().compareTo(amount) < 0)
            return ResponseEntity.badRequest().body("Insufficient balance");

        account.setBalance(account.getBalance().subtract(amount));
        accountService.updateBalance(account, account.getBalance());

        transactionService.logTransaction(account, amount, "WITHDRAW"); // ✅ Log transaction

        return ResponseEntity.ok("Withdrawn successfully");
    }

    // ✅ Transfer money (query params: fromId, toId, amount)
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromId,
                                           @RequestParam Long toId,
                                           @RequestParam BigDecimal amount) {
        Optional<Account> fromOpt = accountService.getById(fromId);
        Optional<Account> toOpt = accountService.getById(toId);
        if (fromOpt.isEmpty() || toOpt.isEmpty()) return ResponseEntity.badRequest().body("Invalid account");

        Account from = fromOpt.get();
        Account to = toOpt.get();

        if (from.getBalance().compareTo(amount) < 0)
            return ResponseEntity.badRequest().body("Insufficient balance");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountService.updateBalance(from, from.getBalance());
        accountService.updateBalance(to, to.getBalance());

        // ✅ Log both directions
        transactionService.logTransaction(from, amount, "TRANSFER_OUT");
        transactionService.logTransaction(to, amount, "TRANSFER_IN");

        return ResponseEntity.ok("Transfer successful");
    }
}

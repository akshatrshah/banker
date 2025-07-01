package com.example.banker.controller;

import com.example.banker.model.User;
import com.example.banker.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class BankingController {

    @Autowired
    private BankingService service;

    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String password = body.get("password");
        return service.register(name, password);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String password = body.get("password");
        return service.login(name, password);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        service.logout(token);
        return "Logged out successfully";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> body) {
        BigDecimal amount = new BigDecimal(body.get("amount"));
        service.deposit(token, amount);
        return "Deposit successful";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> body) {
        BigDecimal amount = new BigDecimal(body.get("amount"));
        service.withdraw(token, amount);
        return "Withdraw successful";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> body) {
        Long toUserId = Long.parseLong(body.get("toUserId"));
        BigDecimal amount = new BigDecimal(body.get("amount"));
        service.transfer(token, toUserId, amount);
        return "Transfer successful";
    }

    // ✅ New: Get full user profile
    @GetMapping("/me")
    public User getProfile(@RequestHeader("Authorization") String token) {
        return service.getCurrentUser(token);
    }

    // ✅ New: Get balance only
    @GetMapping("/balance")
    public BigDecimal getBalance(@RequestHeader("Authorization") String token) {
        return service.getCurrentUser(token).getBalance();
    }
}

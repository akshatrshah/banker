package com.example.banker.service;

import com.example.banker.model.Account;
import com.example.banker.model.User;
import com.example.banker.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(User user) {
        Account account = new Account(UUID.randomUUID().toString(), user);
        return accountRepository.save(account);
    }

    public Optional<Account> getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    public void updateBalance(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    
}

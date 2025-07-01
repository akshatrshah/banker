package com.example.banker.service;

import com.example.banker.model.Account;
import com.example.banker.model.Transaction;
import com.example.banker.model.TransactionType;
import com.example.banker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction recordTransaction(Account account, TransactionType type, BigDecimal amount) {
        Transaction txn = new Transaction(type, amount, account);
        return transactionRepository.save(txn);
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public void logTransaction(Account account, BigDecimal amount, String type) {
    Transaction transaction = new Transaction();
    transaction.setAccount(account);
    transaction.setAmount(amount);
    transaction.setType(TransactionType.valueOf(type));
    transaction.setTimestamp(LocalDateTime.now());
    transactionRepository.save(transaction);
}
}

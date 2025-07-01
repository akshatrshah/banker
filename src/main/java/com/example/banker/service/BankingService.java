package com.example.banker.service;

import com.example.banker.model.User;
import com.example.banker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BankingService {

    @Autowired
    private UserRepository userRepository;

    // token -> userId session store
    private final Map<String, Long> sessions = new ConcurrentHashMap<>();

    public User register(String name, String password) {
        if (name == null || password == null || name.trim().isEmpty() || password.trim().isEmpty()) {
            throw new RuntimeException("Name and password must not be empty");
        }
        if (userRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(name, password);
        return userRepository.save(user);
    }

    public String login(String name, String password) {
        if (name == null || password == null) {
            throw new RuntimeException("Missing credentials");
        }
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = generateToken();
        sessions.put(token, user.getId());
        return token;
    }

    public void logout(String token) {
        sessions.remove(token);
    }

    public void deposit(String token, BigDecimal amount) {
        User user = getUserByToken(token);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    public void withdraw(String token, BigDecimal amount) {
        User user = getUserByToken(token);
        if (user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
    }

    public void transfer(String token, Long toUserId, BigDecimal amount) {
        User fromUser = getUserByToken(token);
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new RuntimeException("Target user not found"));

        if (fromUser.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        fromUser.setBalance(fromUser.getBalance().subtract(amount));
        toUser.setBalance(toUser.getBalance().add(amount));

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    private User getUserByToken(String token) {
        Long userId = sessions.get(token);
        if (userId == null) throw new RuntimeException("Invalid or expired session");
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public User getCurrentUser(String token) {
        return getUserByToken(token);
    }
}

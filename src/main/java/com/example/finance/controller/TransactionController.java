package com.example.finance.controller;

import com.example.finance.model.Transaction;
import com.example.finance.model.User;
import com.example.finance.repository.TransactionRepository;
import com.example.finance.repository.UserRepository;
import com.example.finance.security.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtUtil jwtUtil;

    private User getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return userRepo.findByEmail(email).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody TransactionDTO dto, HttpServletRequest request) {
        User user = getCurrentUser(request);
        Transaction transaction = Transaction.builder()
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .type(dto.getType())
                .date(LocalDate.parse(dto.getDate()))
                .category(dto.getCategory())
                .user(user)
                .build();
        return ResponseEntity.ok(transactionRepo.save(transaction));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAll(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return ResponseEntity.ok(transactionRepo.findByUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        User user = getCurrentUser(request);
        Transaction transaction = transactionRepo.findById(id).orElseThrow();
        if (!transaction.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        transactionRepo.delete(transaction);
        return ResponseEntity.ok("Deleted");
    }

    @Data
    static class TransactionDTO {
        private String title;
        private Double amount;
        private String type;
        private String date;
        private String category;
    }
}

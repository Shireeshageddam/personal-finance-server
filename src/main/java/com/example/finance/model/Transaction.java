package com.example.finance.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double amount;
    private LocalDate date;
    private String type; // "income" or "expense"
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

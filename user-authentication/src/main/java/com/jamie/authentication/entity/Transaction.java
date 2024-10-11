package com.jamie.authentication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key column
    private User user;

    @Enumerated(EnumType.STRING)  // Store enum as a string in DB
    private TransactionType transactionType;

    private String startStation; // Can be NULL for top-ups

    private String destStation; // Can be NULL for top-ups

    private LocalDateTime swipeInTime; // Can be NULL for top-ups

    private LocalDateTime swipeOutTime; // Can be NULL for top-ups

    private double fee; // Amount added in top-up or fare fee

    private double balanceBefore;

    private double balanceAfter;

    private LocalDateTime transTime;

}


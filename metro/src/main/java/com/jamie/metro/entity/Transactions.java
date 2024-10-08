package com.jamie.metro.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {

    private Long transId;

    private Long userId;

    private TransactionType transactionType;

    private Integer startStation; // Can be NULL for top-ups

    private Integer destStation; // Can be NULL for top-ups

    private LocalDateTime swipeInTime; // Can be NULL for top-ups

    private LocalDateTime swipeOutTime; // Can be NULL for top-ups

    private double fee; // Amount added in top-up or fare fee

    private double balanceBefore;

    private double balanceAfter;

    private LocalDateTime transTime;

}
package com.jamie.metro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFareDto {

    private String startStation; // Can be NULL for top-ups

    private String destStation; // Can be NULL for top-ups

    private LocalDateTime swipeInTime; // Can be NULL for top-ups

    private LocalDateTime swipeOutTime; // Can be NULL for top-ups

    private LocalDateTime transTime;

}
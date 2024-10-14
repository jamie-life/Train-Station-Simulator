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
public class TransactionTopUpDto {

    private Long userId;

    private double topUp; // Amount added in top-up

    private LocalDateTime transTime;

}

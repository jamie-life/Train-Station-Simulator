package com.jamie.metro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BalanceUpdateRequestDto {
    private Long userId;
    private Double topUp;
}

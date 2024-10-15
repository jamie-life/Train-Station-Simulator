package com.jamie.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JwtAuthResponseDto {
    private String token;
    private String username;
    private String firstName;
    private double balance;
}

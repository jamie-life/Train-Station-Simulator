package com.jamie.metro.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private double balance;
}

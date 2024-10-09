package com.jamie.authentication.service;

import com.jamie.authentication.dto.LoginDto;
import com.jamie.authentication.dto.RegisterDto;
import org.springframework.http.ResponseEntity;


public interface AuthenticationService {
    ResponseEntity<String> register (RegisterDto registerDto);
    ResponseEntity<String> login (LoginDto loginDto);
}

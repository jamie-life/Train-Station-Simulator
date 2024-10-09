package com.jamie.metro.service;

import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.dto.RegisterDto;
import com.jamie.metro.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    String register (RegisterDto registerDto);
    String login (LoginDto registerDto);
    ResponseEntity<UserDto> getUser (String username);
}

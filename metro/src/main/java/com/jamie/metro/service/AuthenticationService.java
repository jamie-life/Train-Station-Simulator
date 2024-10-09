package com.jamie.metro.service;

import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.dto.RegisterDto;

public interface AuthenticationService {
    String register (RegisterDto registerDto);
    String login(LoginDto registerDto);
}

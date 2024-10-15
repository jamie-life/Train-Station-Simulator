package com.jamie.metro.service;

import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.dto.RegisterDto;
import jakarta.servlet.http.HttpSession;

public interface AuthenticationService {
    String register(RegisterDto registerDto);

    String login(LoginDto loginDto, HttpSession session);

    void logout(HttpSession session);

}

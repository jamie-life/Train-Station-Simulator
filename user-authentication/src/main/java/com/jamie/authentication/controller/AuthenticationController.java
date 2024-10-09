package com.jamie.authentication.controller;

import com.jamie.authentication.dto.LoginDto;
import com.jamie.authentication.dto.RegisterDto;
import com.jamie.authentication.dto.UserDto;
import com.jamie.authentication.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    // Register REST API
    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegisterDto registerDto) {
        return authenticationService.register(registerDto);
    }

    // Login REST API
    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }

    @GetMapping("{username}")
    public ResponseEntity<UserDto> getUser (@PathVariable String username) {
        return authenticationService.getUser(username);
    }

}

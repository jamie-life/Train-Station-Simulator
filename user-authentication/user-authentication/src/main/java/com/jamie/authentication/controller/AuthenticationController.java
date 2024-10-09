package com.jamie.authentication.controller;

import dto.RegisterDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    // Register REST API
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = "Testing";
        System.out.println("Username: " + registerDto.getUsername());
        System.out.println("First Name: " + registerDto.getFirstName());
        System.out.println("Last Name: " + registerDto.getLastName());;
        System.out.println("Email: " + registerDto.getEmail());
        System.out.println("Password: " + registerDto.getPassword());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

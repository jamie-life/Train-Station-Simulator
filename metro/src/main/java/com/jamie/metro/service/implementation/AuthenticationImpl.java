package com.jamie.metro.service.implementation;

import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.dto.RegisterDto;
import com.jamie.metro.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthenticationImpl implements AuthenticationService {


    private final RestTemplate restTemplate;


    @Override
    public String register(RegisterDto registerDto) {
        // Pass RegisterDto to Login API using Rest Template
        String RegisterApiURL = "http://localhost:8082/api/auth/register";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Wrap RegisterDto Object in Http Entity with headers
        HttpEntity<RegisterDto> request = new HttpEntity<>(registerDto, headers);

        // Send the Post Request to Login/Register API
        ResponseEntity<String> response = restTemplate.exchange(
                RegisterApiURL,
                HttpMethod.POST,
                request,
                String.class
        );

        // Handle the response
        if (response.getStatusCode().is2xxSuccessful()) {
            return "User Signed Up and forwarded to Login API";
        } else {
            return "Failed to Sign Up User";
        }
    }

    @Override
    public String login(LoginDto registerDto) {
        return "";
    }
}

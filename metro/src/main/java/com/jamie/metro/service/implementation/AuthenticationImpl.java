package com.jamie.metro.service.implementation;

import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.dto.RegisterDto;
import com.jamie.metro.exception.TrainException;
import com.jamie.metro.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
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

        // Send the Post Request to User Authentication API
        try {
            // Perform the API request
            ResponseEntity<String> response = restTemplate.exchange(
                    RegisterApiURL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Handle 201 Created response
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return "User has been successfully registered";
            }
        } catch (HttpClientErrorException e) {
            // Handle 400 Bad Request
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // Extract the message from the response body
                String errorMessage = e.getResponseBodyAsString();
                return "Error: " + errorMessage;
            }
        }

        // If other issues arise, you can log or handle them
        throw new TrainException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");

    }

    @Override
    public String login(LoginDto registerDto) {
        return "";
    }
}

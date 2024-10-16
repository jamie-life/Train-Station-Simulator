package com.jamie.metro.service.implementation;

import com.jamie.metro.dto.JwtAuthResponseDto;
import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.dto.RegisterDto;
import com.jamie.metro.dto.UserDto;
import com.jamie.metro.exception.TrainException;
import com.jamie.metro.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthenticationImpl implements AuthenticationService {


    private final RestTemplate restTemplate;
    private final String base_api_url = "https://9764172992.xyz/api/auth";
    //private final String base_api_url = "http://localhost:8082/api/auth";
    private ModelMapper modelMapper;

    @Override
    public String register(RegisterDto registerDto) {
        // Pass RegisterDto to Login API using Rest Template
        String RegisterApiURL = base_api_url + "/register";

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
    public String login(LoginDto loginDto, HttpSession session) {
        // Pass RegisterDto to Login API using Rest Template
        String LoginApiURL = base_api_url + "/login";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Wrap LoginDto Object in Http Entity with headers
        HttpEntity<LoginDto> request = new HttpEntity<>(loginDto, headers);

        // Send the Post Request to User Authentication API
        try {
            // Perform the API request
            ResponseEntity<JwtAuthResponseDto> response = restTemplate.exchange(
                    LoginApiURL,
                    HttpMethod.POST,
                    request,
                    JwtAuthResponseDto.class
            );


            // Handle 201 Created response
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JwtAuthResponseDto responseDto = response.getBody();
                UserDto userDto = modelMapper.map(responseDto, UserDto.class);
                session.setAttribute("user", userDto);
                session.setAttribute("token", responseDto.getToken());
                return "User has been successfully Logged In.";
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            return "Error: Incorrect Username Or Password " + errorMessage;
        }

        // If other issues arise, you can log or handle them
        throw new TrainException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    @Override
    public void logout(HttpSession session) {
        // Define the URL for your external server's logout endpoint
        String logoutUrl = "https://9764172992.xyz/logout";

        try {
            // Get the JWT token from the session
            String token = (String) session.getAttribute("token");

            if (token == null) {
                System.err.println("Token is missing, cannot log out");
                return;
            }

            // Create headers and add the Authorization header with the Bearer token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            // Create HttpEntity with the headers (no body needed for logout)
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Send the POST request to the external server's logout endpoint
            ResponseEntity<String> response = restTemplate.exchange(
                    logoutUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // If logout is successful on external server
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Successfully logged out from external system");

                // Clear JWT from the session
                session.removeAttribute("token");
                session.removeAttribute("user");

                // Invalidate the session
                session.invalidate();

            } else {
                System.err.println("Logout failed: " + response.getStatusCode());
            }

        } catch (Exception e) {
            // Handle errors such as communication failure, etc.
            System.err.println("Error during external logout: " + e.getMessage());
        }
    }


}

package com.jamie.metro.service.implementation;

import com.jamie.metro.dto.LoginDto;
import com.jamie.metro.dto.RegisterDto;
import com.jamie.metro.dto.UserDto;
import com.jamie.metro.exception.TrainException;
import com.jamie.metro.service.AuthenticationService;
import lombok.AllArgsConstructor;
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
    public String login(LoginDto loginDto) {
        // Pass RegisterDto to Login API using Rest Template
        String LoginApiURL = "http://localhost:8082/api/auth/login";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Wrap LoginDto Object in Http Entity with headers
        HttpEntity<LoginDto> request = new HttpEntity<>(loginDto, headers);

        // Send the Post Request to User Authentication API
        try {
            // Perform the API request
            ResponseEntity<String> response = restTemplate.exchange(
                    LoginApiURL,
                    HttpMethod.POST,
                    request,
                    String.class
            );


            // Handle 201 Created response
            if (response.getStatusCode() == HttpStatus.ACCEPTED) {
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
    public ResponseEntity<UserDto> getUser(String username) {
        // Define the URL for the user details endpoint in the authentication API
        String userApiURL = "http://localhost:8082/api/auth/" + username; // Adjust the URL as necessary

        // Create headers for the request if needed (e.g., for authentication)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Add any other headers required, such as Authorization

        // Create the HttpEntity object to include the headers
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // Send the GET request to the user API
            ResponseEntity<UserDto> response = restTemplate.exchange(
                    userApiURL,
                    HttpMethod.GET,
                    requestEntity,
                    UserDto.class // Specify the response type
            );

            // Check if the response is successful
            if (response.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
            } else {
                // Handle other status codes if necessary
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException e) {
            // Handle errors (e.g., user not found, unauthorized access)
            String errorMessage = e.getResponseBodyAsString();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Handle other exceptions (network issues, etc.)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

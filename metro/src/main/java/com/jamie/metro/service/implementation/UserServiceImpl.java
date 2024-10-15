package com.jamie.metro.service.implementation;

import com.jamie.metro.dto.TransactionDto;
import com.jamie.metro.dto.TransactionFareDto;
import com.jamie.metro.dto.TransactionTopUpDto;
import com.jamie.metro.exception.TrainException;
import com.jamie.metro.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;
    //private final String base_api_url = "https://9764172992.xyz/api/user";
    private final String base_api_url = "http://localhost:8082/api/user";
    private ModelMapper modelMapper;
    private HttpSession session;

    @Override
    public Double addTransaction(TransactionFareDto transactionFareDto) {
        // API URL
        String apiUrl = base_api_url + "/add-journey";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + session.getAttribute("token"));

        // Wrap RegisterDto Object in Http Entity with headers
        HttpEntity<TransactionFareDto> request = new HttpEntity<>(transactionFareDto, headers);


        try {
            // Send POST request and receive response
            ResponseEntity<Double> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    Double.class // The response type is String.class
            );

            // Check if the response is successful (HTTP 200 OK)
            if (response.getStatusCode() == HttpStatus.CREATED) {
                // Return the updated balance
                return response.getBody();
            }

        } catch (HttpClientErrorException e) {
            // Handle specific HTTP status errors
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // Log or throw an exception with relevant details
                String errorMessage = e.getResponseBodyAsString();
                throw new TrainException(HttpStatus.BAD_REQUEST, "Bad request: " + errorMessage);
            }

            // Add more specific status code handling if needed

        } catch (Exception e) {
            // Log or handle other unexpected errors
            throw new TrainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        // If the response was not 201 Created or an exception occurred, handle it
        throw new TrainException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Add Journey");
    }

    @Override
    public List<TransactionDto> getTransactions() {
        // Define the URL for the user details endpoint in the authentication API
        String userApiURL = base_api_url + "/get-transactions"; // Adjust the URL as necessary

        // Create headers for the request if needed (e.g., for authentication)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + session.getAttribute("token"));

        // Create the HttpEntity object to include the headers
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // Send the GET request to the user API
            ResponseEntity<List<TransactionDto>> response = restTemplate.exchange(
                    userApiURL,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    } // Specify the response type
            );

            // Check if the response is successful and return the list
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                // Return an empty list or handle as needed
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException e) {
            // Log the error and return an empty list
            String errorMessage = e.getResponseBodyAsString();
            return Collections.emptyList();
        } catch (Exception e) {
            // Handle other exceptions (network issues, etc.) and return an empty list
            return Collections.emptyList();
        }
    }


    @Override
    public double addBalance(TransactionTopUpDto transactionTopUpDto) {
        // API URL
        String apiUrl = base_api_url + "/add-funds";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + session.getAttribute("token"));

        // Wrap RegisterDto Object in Http Entity with headers
        HttpEntity<TransactionTopUpDto> request = new HttpEntity<>(transactionTopUpDto, headers);


        try {
            // Send POST request and receive response
            ResponseEntity<Double> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    Double.class // The response type is Double.class
            );

            // Check if the response is successful (HTTP 200 OK)
            if (response.getStatusCode() == HttpStatus.OK) {
                // Return the updated balance
                return response.getBody();
            }

        } catch (HttpClientErrorException e) {
            // Handle specific HTTP status errors
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // Log or throw an exception with relevant details
                String errorMessage = e.getResponseBodyAsString();
                throw new TrainException(HttpStatus.BAD_REQUEST, "Bad request: " + errorMessage);
            }

            // Add more specific status code handling if needed

        } catch (Exception e) {
            // Log or handle other unexpected errors
            throw new TrainException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        // If the response was not 201 Created or an exception occurred, handle it
        throw new TrainException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update balance.");
    }
}

package com.jamie.authentication.service;

import com.jamie.authentication.dto.*;
import org.springframework.http.ResponseEntity;


public interface AuthenticationService {
    ResponseEntity<String> register (RegisterDto registerDto);
    ResponseEntity<String> login (LoginDto loginDto);
    ResponseEntity<UserDto> getUser (String username);
    ResponseEntity<Double> topUpBalance (TransactionTopUpDto transactionTopUpDto);
    ResponseEntity<Double> addJourney (TransactionFareDto transactionFareDto);
}

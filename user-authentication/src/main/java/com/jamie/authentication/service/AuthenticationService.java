package com.jamie.authentication.service;

import com.jamie.authentication.dto.*;
import com.jamie.authentication.entity.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AuthenticationService {
    ResponseEntity<String> register (RegisterDto registerDto);
    ResponseEntity<String> login (LoginDto loginDto);
    ResponseEntity<UserDto> getUser (String username);
    ResponseEntity<Double> topUpBalance (TransactionTopUpDto transactionTopUpDto);
    ResponseEntity<Double> addJourney (TransactionFareDto transactionFareDto);
    ResponseEntity<List<TransactionDto>> getTransactions (Long id);
}

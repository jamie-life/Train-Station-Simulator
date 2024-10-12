package com.jamie.metro.service;

import com.jamie.metro.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthenticationService {
    String register (RegisterDto registerDto);
    String login (LoginDto registerDto);
    Double addTransaction (TransactionFareDto transactionFareDto);
    double addBalance (TransactionTopUpDto transactionTopUpDto);
    ResponseEntity<UserDto> getUser (String username);
    List<TransactionDto> getTransactions (Long id);

}

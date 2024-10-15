package com.jamie.authentication.service;

import com.jamie.authentication.dto.TransactionDto;
import com.jamie.authentication.dto.TransactionFareDto;
import com.jamie.authentication.dto.TransactionTopUpDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<Double> topUpBalance(TransactionTopUpDto transactionTopUpDto);

    ResponseEntity<Double> addJourney(TransactionFareDto transactionFareDto);

    ResponseEntity<List<TransactionDto>> getTransactions();
}

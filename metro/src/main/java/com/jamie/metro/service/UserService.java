package com.jamie.metro.service;

import com.jamie.metro.dto.TransactionDto;
import com.jamie.metro.dto.TransactionFareDto;
import com.jamie.metro.dto.TransactionTopUpDto;

import java.util.List;

public interface UserService {
    Double addTransaction(TransactionFareDto transactionFareDto);

    double addBalance(TransactionTopUpDto transactionTopUpDto);

    List<TransactionDto> getTransactions();
}

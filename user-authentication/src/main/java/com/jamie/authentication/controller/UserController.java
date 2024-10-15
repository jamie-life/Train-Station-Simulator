package com.jamie.authentication.controller;

import com.jamie.authentication.dto.TransactionDto;
import com.jamie.authentication.dto.TransactionFareDto;
import com.jamie.authentication.dto.TransactionTopUpDto;
import com.jamie.authentication.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    // Add Funds REST API
    @PostMapping("/add-funds")
    public ResponseEntity<Double> addFunds(@RequestBody TransactionTopUpDto transactionTopUpDto) {
        return userService.topUpBalance(transactionTopUpDto);
    }

    @PostMapping("/add-journey")
    public ResponseEntity<Double> addJourney(@RequestBody TransactionFareDto transactionFareDto) {
        return userService.addJourney(transactionFareDto);
    }

    @GetMapping("/get-transactions")
    public ResponseEntity<List<TransactionDto>> addJourney() {
        return userService.getTransactions();
    }

}

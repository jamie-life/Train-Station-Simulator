package com.jamie.authentication.controller;

import com.jamie.authentication.dto.TransactionFareDto;
import com.jamie.authentication.dto.TransactionTopUpDto;
import com.jamie.authentication.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController {

    private AuthenticationService authenticationService;

    // Add Funds REST API
    @PostMapping("/add-funds")
    public ResponseEntity<Double> addFunds(@RequestBody TransactionTopUpDto transactionTopUpDto) {
        return authenticationService.topUpBalance(transactionTopUpDto);
    }

    @PostMapping("/add-journey")
    public ResponseEntity<Double> addJourney(@RequestBody TransactionFareDto transactionFareDto) {
        return authenticationService.addJourney(transactionFareDto);
    }
}

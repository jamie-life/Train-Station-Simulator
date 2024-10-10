package com.jamie.authentication.controller;

import com.jamie.authentication.dto.BalanceUpdateRequestDto;
import com.jamie.authentication.dto.RegisterDto;
import com.jamie.authentication.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController {

    private AuthenticationService authenticationService;

    // Add Funds REST API
    @PostMapping("/add-funds")
    public ResponseEntity<Double> addFunds(@RequestBody BalanceUpdateRequestDto balanceUpdateRequestDto) {
        return authenticationService.topUpBalance(balanceUpdateRequestDto.getUserId(),
                balanceUpdateRequestDto.getTopUp());
    }
}

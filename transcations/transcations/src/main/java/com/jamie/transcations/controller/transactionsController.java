package com.jamie.transcations.controller;

import com.jamie.transcations.entity.Transactions;
import com.jamie.transcations.service.TransactionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/train")
public class transactionsController {

    private TransactionsService transactionsService;

    // Register REST API
    @PostMapping("/add-transaction")
    public RequestEntity<String> addTransaction (@RequestBody Transactions transactions) {
        return null;
    }




}

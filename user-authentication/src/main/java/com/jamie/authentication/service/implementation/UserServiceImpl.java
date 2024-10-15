package com.jamie.authentication.service.implementation;

import com.jamie.authentication.dto.TransactionDto;
import com.jamie.authentication.dto.TransactionFareDto;
import com.jamie.authentication.dto.TransactionTopUpDto;
import com.jamie.authentication.entity.Transaction;
import com.jamie.authentication.entity.TransactionType;
import com.jamie.authentication.entity.User;
import com.jamie.authentication.repository.TransactionRepository;
import com.jamie.authentication.repository.UserRepository;
import com.jamie.authentication.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.jamie.authentication.utility.SecurityUtils.getAuthenticatedUsername;
import static java.lang.Math.abs;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public ResponseEntity<Double> topUpBalance(TransactionTopUpDto transactionTopUpDto) {

        List<Double> topUpValues = List.of(5.0, 10.0, 20.0, 50.0);
        if (!topUpValues.contains(transactionTopUpDto.getTopUp())) {
            return new ResponseEntity<>(transactionTopUpDto.getTopUp(), HttpStatus.BAD_REQUEST);
        }

        // Fetch User by Username and verify it exist.
        User user = userRepository.findByUsername(getAuthenticatedUsername()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle the case when the user is not found
        }

        // Update Balance and Assign Values to be save in Transaction database.
        double currentBalance = user.getBalance();
        double newBalance = currentBalance + transactionTopUpDto.getTopUp();
        user.setBalance(newBalance);

        // Build Transaction Object to be saved.
        Transaction newTransaction = new Transaction();
        newTransaction.setUser(user);
        newTransaction.setTransactionType(TransactionType.TOP_UP);
        newTransaction.setStartStation(null);
        newTransaction.setDestStation(null);
        newTransaction.setSwipeInTime(null);
        newTransaction.setSwipeOutTime(null);
        newTransaction.setFee(transactionTopUpDto.getTopUp());
        newTransaction.setBalanceBefore(currentBalance);
        newTransaction.setBalanceAfter(newBalance);
        newTransaction.setTransTime(transactionTopUpDto.getTransTime());

        transactionRepository.save(newTransaction);
        userRepository.save(user);

        return new ResponseEntity<>(user.getBalance(), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Double> addJourney(TransactionFareDto transactionFareDto) {
        List<String> station = Arrays.asList("Station 1", "Station 2", "Station 3", "Station 4");

        if (!station.contains(transactionFareDto.getStartStation()) || !station.contains(transactionFareDto.getDestStation())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Fetch User by Username and verify it exist.
        User user = userRepository.findByUsername(getAuthenticatedUsername()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle the case when the user is not found
        }

        // Calculate Balance & Fee
        double feeAmount = abs(station.indexOf(transactionFareDto.getDestStation()) - station.indexOf(transactionFareDto.getStartStation())) * 5;
        double currentBalance = user.getBalance();
        if (currentBalance < feeAmount) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double newBalance = currentBalance - feeAmount;
        user.setBalance(newBalance);

        // Build Transaction Object to be saved.
        Transaction newTransaction = new Transaction();
        newTransaction.setUser(user);
        newTransaction.setTransactionType(TransactionType.FARE);
        newTransaction.setStartStation(transactionFareDto.getStartStation());
        newTransaction.setDestStation(transactionFareDto.getDestStation());
        newTransaction.setSwipeInTime(transactionFareDto.getSwipeInTime());
        newTransaction.setSwipeOutTime(transactionFareDto.getSwipeOutTime());
        newTransaction.setFee(feeAmount);
        newTransaction.setBalanceBefore(currentBalance);
        newTransaction.setBalanceAfter(newBalance);
        newTransaction.setTransTime(transactionFareDto.getTransTime());

        transactionRepository.save(newTransaction);
        userRepository.save(user);

        return new ResponseEntity<>(newBalance, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        // Fetch User by Username and verify it exist.
        User user = userRepository.findByUsername(getAuthenticatedUsername()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle the case when the user is not found
        }

        List<Transaction> transactionLists = transactionRepository.findByUserUserId(user.getUserId());
        List<TransactionDto> transactionListsDto = transactionLists.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();
        return new ResponseEntity<>(transactionListsDto, HttpStatus.OK);
    }
}

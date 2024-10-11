package com.jamie.authentication.service.implementation;

import com.jamie.authentication.dto.*;
import com.jamie.authentication.entity.Transaction;
import com.jamie.authentication.entity.TransactionType;
import com.jamie.authentication.entity.User;
import com.jamie.authentication.repository.TransactionRepository;
import com.jamie.authentication.repository.UserRepository;
import com.jamie.authentication.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

@Service
@AllArgsConstructor
public class AuthenticationImpl implements AuthenticationService {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<String> register(RegisterDto registerDto) {
        // Check If Username Exist
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>( "Username is already in use", HttpStatus.BAD_REQUEST);
        }
        // Check if Email Exist
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>( "Email is already in use", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setUsername(registerDto.getUsername());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setBalance(0);

        userRepository.save(newUser);

        return new ResponseEntity<>("User Registered Successfully!", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User Logged In Successfully!", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<UserDto> getUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle the case when the user is not found
        }

        UserDto userDto = new UserDto();

        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getUserId());
        userDto.setBalance(user.getBalance());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Double> topUpBalance(TransactionTopUpDto transactionTopUpDto) {

        // Fetch User by Id and verify it exist.
        User user = userRepository.findById(transactionTopUpDto.getUserId()).orElse(null);
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

        if (station.indexOf(transactionFareDto.getStartStation()) == -1 || station.indexOf(transactionFareDto.getDestStation()) == -1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Fetch User by Id and verify it exist.
        User user = userRepository.findById(transactionFareDto.getUserId()).orElse(null);
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
}

package com.jamie.authentication.service.implementation;

import com.jamie.authentication.dto.LoginDto;
import com.jamie.authentication.dto.RegisterDto;
import com.jamie.authentication.entity.User;
import com.jamie.authentication.repository.UserRepository;
import com.jamie.authentication.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationImpl implements AuthenticationService {

    private UserRepository userRepository;

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
        newUser.setPassword(registerDto.getPassword());
        newUser.setBalance(0);

        userRepository.save(newUser);

        return new ResponseEntity<>("User Registered Successfully!", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> login(LoginDto loginDto) {
        return new ResponseEntity<>("User Logged In Successfully!", HttpStatus.ACCEPTED);
    }
}

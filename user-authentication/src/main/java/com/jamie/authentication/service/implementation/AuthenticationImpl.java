package com.jamie.authentication.service.implementation;

import com.jamie.authentication.dto.JwtAuthResponseDto;
import com.jamie.authentication.dto.LoginDto;
import com.jamie.authentication.dto.RegisterDto;
import com.jamie.authentication.entity.User;
import com.jamie.authentication.repository.UserRepository;
import com.jamie.authentication.security.token.JwtProvider;
import com.jamie.authentication.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationImpl implements AuthenticationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Override
    public ResponseEntity<String> register(RegisterDto registerDto) {
        // Check If Username Exist
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
        }
        // Check if Email Exist
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email is already in use", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<JwtAuthResponseDto> login(LoginDto loginDto) {

        // Authenticate The User Else It Will Throw An Error
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

        // Authenticated User is Now Stored For The Request in the Secuirty Context.
        SecurityContextHolder.getContext().setAuthentication(authentication);


        User user = userRepository.findByUsername(loginDto.getUsername()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Handle the case when the user is not found
        }

        String token = jwtProvider.generateToken(authentication);
        JwtAuthResponseDto responseDto = new JwtAuthResponseDto();

        responseDto.setToken(token);
        responseDto.setUsername(user.getUsername());
        responseDto.setFirstName(user.getFirstName());
        responseDto.setBalance(user.getBalance());


        return ResponseEntity.ok(responseDto);
    }

}

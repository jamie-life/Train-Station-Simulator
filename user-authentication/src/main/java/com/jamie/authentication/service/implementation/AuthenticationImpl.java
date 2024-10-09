package com.jamie.authentication.service.implementation;

import com.jamie.authentication.dto.LoginDto;
import com.jamie.authentication.dto.RegisterDto;
import com.jamie.authentication.dto.UserDto;
import com.jamie.authentication.entity.User;
import com.jamie.authentication.repository.UserRepository;
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
}

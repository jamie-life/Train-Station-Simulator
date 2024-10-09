package com.jamie.authentication.repository;

import com.jamie.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
}


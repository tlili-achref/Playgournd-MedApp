package com.medapp.backend.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.medapp.backend.model.Role;
import com.medapp.backend.model.User;
import com.medapp.backend.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email , String password , String nom , String prenom, Role role){
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email , hashedPassword , nom , prenom , role ,true , LocalDateTime.now() , null);
        return userRepository.save(user);
    }
    
}

package com.medapp.backend.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


import com.medapp.backend.model.Role;
import com.medapp.backend.model.User;

public class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_creeUnTokenValide(){
        //
        User user = new User("medcin@medapp.com" , "hashedPassword" , "Dupont" , "Jean" , Role.MEDECIN , true , LocalDateTime.now() , null);

        //
        String token = jwtService.generateToken(user);
        //
        assertNotNull(token);


    }
    
}

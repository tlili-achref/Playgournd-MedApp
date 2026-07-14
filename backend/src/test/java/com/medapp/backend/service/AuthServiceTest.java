package com.medapp.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.medapp.backend.model.Role;
import com.medapp.backend.model.User;
import com.medapp.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_creeUnUtilisateur_siEmailNonUtilise() {
        String email = "medecin@medapp.com";
        String motDePasse = "MotDePasse123!";

        when(passwordEncoder.encode(motDePasse)).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 
        User utilisateurCree = authService.register(email, motDePasse, "Dupont", "Jean", Role.MEDECIN);

        assertEquals(email, utilisateurCree.getEmail());
        assertNotEquals(motDePasse, utilisateurCree.getPasswordHash());
        assertEquals("hashedPassword123", utilisateurCree.getPasswordHash());
        verify(userRepository).save(any(User.class));
    }
    
}

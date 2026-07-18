package com.medapp.backend.repository;

import com.medapp.backend.model.Role;
import com.medapp.backend.model.User;
import org.springframework.dao.DuplicateKeyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DataMongoTest
@Testcontainers
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4");

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void nettoyageBase() {
        userRepository.deleteAll();
    }

    @Test
    void sauvegarde_puis_retrouve_utilisateur_par_email(){
         User user = new User("test@medapp.com" , "hashedpassword", "Dupond" , "Jean", Role.MEDECIN , true , LocalDateTime.now() , null);
         userRepository.save(user);
         
         Optional<User> retrievedUser = userRepository.findByEmail("test@medapp.com");
         assertTrue(retrievedUser.isPresent());
         assertEquals("Dupond", retrievedUser.get().getNom());
    }

    @Test
    void sauvegarde_lanceExcetption_siEmailDejaExistant(){
        User user1 =   new User("test@medapp.com" , "hashedpassword1", "Dupond" , "Jean", Role.MEDECIN , true , LocalDateTime.now() , null);
        User user2 = new User("test@medapp.com" , "hashedpassword2", "martin" , "Jean", Role.MEDECIN , true , LocalDateTime.now() , null);

        userRepository.save(user1);

        assertThrows(DuplicateKeyException.class, () -> userRepository.save(user2));
    }
    
}

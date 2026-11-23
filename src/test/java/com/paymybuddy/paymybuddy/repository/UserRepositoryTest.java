package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_retourneLUtilisateurEnregistre() {
        User user = new User();
        user.setUsername("Test");
        user.setEmail("test@example.com");
        user.setPassword("hash");
        userRepository.save(user);

        assertTrue(userRepository.findByEmail("test@example.com").isPresent());
        assertFalse(userRepository.findByEmail("inconnu@example.com").isPresent());
    }

    @Test
    void existsByEmail_retourneTrueSeulementPourUnEmailEnregistre() {
        User user = new User();
        user.setUsername("Test2");
        user.setEmail("test2@example.com");
        user.setPassword("hash");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("test2@example.com"));
        assertFalse(userRepository.existsByEmail("inconnu@example.com"));
    }
}

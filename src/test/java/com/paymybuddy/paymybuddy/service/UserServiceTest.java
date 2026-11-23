package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void register_enregistreLUtilisateurAvecUnMotDePasseHache() {
        User user = userService.register("Nina", "nina@gmail.com", "motdepasse123");

        assertNotNull(user.getId());
        assertNotEquals("motdepasse123", user.getPassword());
        assertTrue(passwordEncoder.matches("motdepasse123", user.getPassword()));
        assertTrue(userRepository.existsByEmail("nina@gmail.com"));
    }

    @Test
    void register_refuseUnEmailDejaUtilise() {
        userService.register("Nina", "nina@gmail.com", "motdepasse123");

        assertThrows(IllegalArgumentException.class,
                () -> userService.register("Autre", "nina@gmail.com", "autremotdepasse"));
    }


    @Test
    void addConnection_ajouteLaRelation() {
        User user = userService.register("Nina", "nina@gmail.com", "motdepasse123");
        User friend = userService.register("Paula", "paula@gmail.com", "motdepasse123");

        userService.addConnection(user.getId(), "Paula@gmail.com");

        User reloaded = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(1, reloaded.getConnections().size());
        assertEquals(friend.getId(), reloaded.getConnections().get(0).getId());
    }

    @Test
    void addConnection_refuseUnEmailInconnu() {
        User user = userService.register("Nina", "nina@gmail.com", "motdepasse123");

        assertThrows(IllegalArgumentException.class,
                () -> userService.addConnection(user.getId(), "inconnu@gmail.com"));
    }

    @Test
    void addConnection_refuseDeSAjouterSoiMeme() {
        User user = userService.register("Nina", "nina@gmail.com", "motdepasse123");

        assertThrows(IllegalArgumentException.class,
                () -> userService.addConnection(user.getId(), "nina@gmail.com"));
    }

    @Test
    void addConnection_refuseUnDoublon() {
        User user = userService.register("Nina", "nina@gmail.com", "motdepasse123");
        userService.register("Paula", "paula@gmail.com", "motdepasse123");

        userService.addConnection(user.getId(), "paula@gmail.com");

        assertThrows(IllegalArgumentException.class,
                () -> userService.addConnection(user.getId(), "paula@gmail.com"));
    }
}

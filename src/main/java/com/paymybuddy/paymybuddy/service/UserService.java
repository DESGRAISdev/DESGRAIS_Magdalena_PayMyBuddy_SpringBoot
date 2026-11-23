package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String username, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Cette adresse e-mail est déjà utilisée.");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    @Transactional
    public void addConnection(Integer userId, String friendEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException("Aucun utilisateur avec cette adresse e-mail."));

        if (friend.getId().equals(user.getId())) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous ajouter vous-même.");
        }
        if (user.getConnections().contains(friend)) {
            throw new IllegalArgumentException("Cette relation existe déjà.");
        }
        user.getConnections().add(friend);
    }
}

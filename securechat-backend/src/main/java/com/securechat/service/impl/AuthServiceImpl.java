package com.securechat.service.impl;

/*
 * Rôle :
 * Implémentation réelle de AuthService.
 *
 * À faire :
 * - Vérifier si email existe déjà
 * - Hasher le mot de passe avec BCrypt
 * - Sauvegarder User
 * - Générer JWT après login
 *
 * Couche :
 * Service Implementation
 */


import com.securechat.dto.AuthResponse;
import com.securechat.dto.LoginRequest;
import com.securechat.dto.RegisterRequest;
import com.securechat.model.User;
import com.securechat.repository.UserRepository;
import com.securechat.security.JwtUtils;
import com.securechat.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username already exists";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "User created";
    }


    @Override
    public AuthResponse login(LoginRequest request) {

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

}


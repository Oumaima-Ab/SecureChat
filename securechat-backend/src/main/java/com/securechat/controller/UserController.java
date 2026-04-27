package com.securechat.controller;

import com.securechat.dto.UpdateUserRequest;
import com.securechat.model.User;
import com.securechat.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userToMap(currentUser);
    }

    @GetMapping
    public List<Map<String, Object>> searchUsers(
            @RequestParam(name = "query", defaultValue = "") String query,
            Authentication authentication
    ) {
        String normalizedQuery = query.trim();

        return userRepository
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(normalizedQuery, normalizedQuery)
                .stream()
                .filter(user -> !user.getEmail().equalsIgnoreCase(authentication.getName()))
                .map(this::userToMap)
                .toList();
    }

    @PutMapping("/me")
    public Map<String, Object> updateMe(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication
    ) {
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!currentUser.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (!currentUser.getUsername().equalsIgnoreCase(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        currentUser.setUsername(request.getUsername());
        currentUser.setEmail(request.getEmail());
        currentUser.setPublicKey(request.getPublicKey());

        User savedUser = userRepository.save(currentUser);
        return userToMap(savedUser);
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("publicKey", user.getPublicKey());
        result.put("createdAt", user.getCreatedAt());
        return result;
    }
}

package com.securechat.service.impl;

import com.securechat.dto.KeyPairRequest;
import com.securechat.model.User;
import com.securechat.model.UserKey;
import com.securechat.repository.UserKeyRepository;
import com.securechat.repository.UserRepository;
import com.securechat.service.UserKeyService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserKeyServiceImpl implements UserKeyService {

    private final UserRepository userRepository;
    private final UserKeyRepository userKeyRepository;

    public UserKeyServiceImpl(UserRepository userRepository, UserKeyRepository userKeyRepository) {
        this.userRepository = userRepository;
        this.userKeyRepository = userKeyRepository;
    }

    @Override
    public String upsertMyKeyPair(KeyPairRequest request) {
        User currentUser = getAuthenticatedUser();

        UserKey key = userKeyRepository.findByUserAndActiveTrue(currentUser)
                .orElseGet(UserKey::new);

        key.setUser(currentUser);
        key.setPublicKey(request.getPublicKey());
        key.setPrivateKey(request.getPrivateKey());

        if (request.getAlgorithm() != null && !request.getAlgorithm().isBlank()) {
            key.setAlgorithm(request.getAlgorithm());
        }

        key.setExpiresAt(request.getExpiresAt());
        key.setActive(true);

        userKeyRepository.save(key);
        return "Key pair saved";
    }

    @Override
    public String getMyPublicKey() {
        User currentUser = getAuthenticatedUser();
        return userKeyRepository.findByUserAndActiveTrue(currentUser)
                .map(UserKey::getPublicKey)
                .orElseThrow(() -> new RuntimeException("Active key pair not found"));
    }

    @Override
    public String getPublicKeyByIdentifier(String identifier) {
        return userKeyRepository.findByUserEmailAndActiveTrue(identifier)
                .or(() -> userKeyRepository.findByUserUsernameAndActiveTrue(identifier))
                .map(UserKey::getPublicKey)
                .orElseThrow(() -> new RuntimeException("Public key not found"));
    }

    @Override
    public String deactivateMyKeyPair() {
        User currentUser = getAuthenticatedUser();

        UserKey key = userKeyRepository.findByUserAndActiveTrue(currentUser)
                .orElseThrow(() -> new RuntimeException("Active key pair not found"));

        key.setActive(false);
        userKeyRepository.save(key);
        return "Key pair deactivated";
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new RuntimeException("Unauthorized");
        }

        String currentUserEmail = authentication.getName();

        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}

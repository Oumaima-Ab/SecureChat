package com.securechat.controller;

import com.securechat.dto.KeyPairRequest;
import com.securechat.service.UserKeyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keys")
public class KeyController {

    private final UserKeyService userKeyService;

    public KeyController(UserKeyService userKeyService) {
        this.userKeyService = userKeyService;
    }

    @PostMapping("/me")
    public ResponseEntity<String> upsertMyKeys(@Valid @RequestBody KeyPairRequest request) {
        return ResponseEntity.ok(userKeyService.upsertMyKeyPair(request));
    }

    @GetMapping("/me/public")
    public ResponseEntity<String> getMyPublicKey() {
        return ResponseEntity.ok(userKeyService.getMyPublicKey());
    }

    @GetMapping("/public/{identifier}")
    public ResponseEntity<String> getPublicKey(@PathVariable String identifier) {
        return ResponseEntity.ok(userKeyService.getPublicKeyByIdentifier(identifier));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deactivateMyKeyPair() {
        return ResponseEntity.ok(userKeyService.deactivateMyKeyPair());
    }
}

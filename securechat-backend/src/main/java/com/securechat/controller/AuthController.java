package com.securechat.controller;

/*
 * Rôle :
 * Expose les endpoints HTTP pour inscription et connexion.
 *
 * Endpoints prévus :
 * - POST /auth/register
 * - POST /auth/login
 *
 * Règle :
 * Le controller ne contient pas la logique métier.
 * Il appelle AuthService.
 *
 * Couche :
 * Controller / Présentation
 */
import com.securechat.dto.AuthResponse;
import com.securechat.dto.LoginRequest;
import com.securechat.dto.RegisterRequest;
import com.securechat.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        String result = authService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
